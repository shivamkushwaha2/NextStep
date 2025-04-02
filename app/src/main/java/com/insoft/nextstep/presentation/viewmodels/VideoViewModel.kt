package com.insoft.nextstep.presentation.viewmodels

import android.content.Context
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import com.insoft.nextstep.data.model.VideoModel
import com.insoft.nextstep.data.model.VideoResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.insoft.nextstep.domain.usecase.GetVideosUsecase
import kotlinx.coroutines.Dispatchers

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val getVideosUseCase: GetVideosUsecase
) : ViewModel() {

    private val _videos = MutableStateFlow<List<VideoResponse>>(emptyList())
    val videos: StateFlow<List<VideoResponse>> = _videos

    // Player cache with max 3 players
    private val playerCache = mutableMapOf<String, ExoPlayer>()

    fun getExoPlayer(context: Context, videoUrl: String): ExoPlayer {
        return playerCache[videoUrl] ?: createPlayer(context, videoUrl).also {
            playerCache[videoUrl] = it
            it.repeatMode = Player.REPEAT_MODE_ONE
            it.playWhenReady = true
        }
    }

    @OptIn(UnstableApi::class)
    private fun createPlayer(context: Context, videoUrl: String): ExoPlayer {
        return ExoPlayer.Builder(context)
            .setLoadControl(
                DefaultLoadControl.Builder()
                    .setBufferDurationsMs(5000, 10000, 2000, 5000)
                    .build()
            )
            .build()
            .apply {
                setMediaItem(MediaItem.fromUri(videoUrl))
                prepare()
            }
    }

    fun preloadVideo(context: Context, videoUrl: String) {
        viewModelScope.launch(Dispatchers.Main) { // 🔥 Run on Main Thread
            getExoPlayer(context, videoUrl)
        }
    }

    override fun onCleared() {
        playerCache.values.forEach { it.release() }
        playerCache.clear()
        super.onCleared()
    }

    init {
        fetchVideos()
    }

    private fun fetchVideos() {
        viewModelScope.launch {
            _videos.value = getVideosUseCase.invoke()
        }
    }
}
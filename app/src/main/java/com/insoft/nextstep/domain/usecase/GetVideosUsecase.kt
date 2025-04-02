package com.insoft.nextstep.domain.usecase

import androidx.lifecycle.ViewModel
import com.insoft.nextstep.data.model.VideoModel
import com.insoft.nextstep.data.model.VideoResponse
import com.insoft.nextstep.domain.repository.VideoRepository
import javax.inject.Inject

class GetVideosUsecase @Inject constructor(
    private val repository: VideoRepository
) {
    suspend operator fun invoke(): List<VideoResponse> {
        return repository.getVideos()
    }
}
package com.insoft.nextstep.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.insoft.nextstep.data.WebSocketManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.Response
import org.json.JSONObject

class WebSocketViewModel : ViewModel() {
    private val _likesFlow = MutableStateFlow<Map<String, Int>>(emptyMap())
    val likesFlow: StateFlow<Map<String, Int>> = _likesFlow

    private val _commentsFlow = MutableStateFlow<Map<String, Int>>(emptyMap())  // ✅ Store as Int
    val commentsFlow: StateFlow<Map<String, Int>> = _commentsFlow

    init {
        println("WebSocket init")
        WebSocketManager.connectWebSocket()
        viewModelScope.launch {
            WebSocketManager.commentsFlow.collect { updatedComments ->
                _commentsFlow.value = updatedComments
            }
        }
        viewModelScope.launch {
            WebSocketManager.likesFlow.collectLatest { updatedLikes ->
                _likesFlow.value = updatedLikes
            }
        }
    }

        fun sendLike(videoId: String, userId: String, isLike: Boolean) {
            WebSocketManager.sendLike(videoId, userId, isLike)
        }

        fun sendComment(videoId: String, userId: String, commentText: String) {
            WebSocketManager.sendComment(videoId, userId, commentText)
        }

        override fun onCleared() {
            super.onCleared()
            WebSocketManager.closeWebSocket()
        }
}

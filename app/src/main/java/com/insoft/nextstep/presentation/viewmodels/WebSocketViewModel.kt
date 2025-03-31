package com.insoft.nextstep.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.insoft.nextstep.data.WebSocketManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.Response
import org.json.JSONObject

class WebSocketViewModel : ViewModel() {
    private val _likesFlow = MutableStateFlow<Map<String, Int>>(emptyMap())
    val likesFlow: StateFlow<Map<String, Int>> = _likesFlow

    private val _commentsFlow = MutableStateFlow<Map<String, Int>>(emptyMap())
    val commentsFlow: StateFlow<Map<String, Int>> = _commentsFlow

    init {
        println("WebSocket init")
        WebSocketManager.connectWebSocket()
        viewModelScope.launch {
            WebSocketManager.likesFlow.collectLatest { updatedLikes ->
                _likesFlow.value = updatedLikes
            }
            WebSocketManager.likesFlow.collectLatest { comments ->
                _likesFlow.value = comments
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

//class WebSocketViewModel : ViewModel() {
//    private val _likesFlow = MutableStateFlow<Map<String, Int>>(emptyMap())
//    val likesFlow: StateFlow<Map<String, Int>> = _likesFlow
//
//    private val _commentsFlow = MutableStateFlow<Map<String, Int>>(emptyMap())
//    val commentsFlow: StateFlow<Map<String, Int>> = _commentsFlow
//
//    init {
//        println("WebSocket init")
//        setupWebSocket()
//    }
//
//    private fun setupWebSocket() {
//        WebSocketManager.connectWebSocket("wss://nextstepbackend.onrender.com/socket.io/?EIO=4&transport=websocket", object : WebSocketListener() {
//            override fun onOpen(webSocket: WebSocket, response: Response) {
//                println("✅ WebSocket Connected")
//            }
//
//            override fun onMessage(webSocket: WebSocket, text: String) {
//                println("✅onMessage $text")
//                handleIncomingMessage(text)
//            }
//
//            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
//                println("⚠️ WebSocket Closing: $reason")
//            }
//
//            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
//                println("❌ WebSocket Error: ${t.message}")
//            }
//        })
//    }
//    private fun handleIncomingMessage(message: String) {
//        try {
//            // Ignore non-JSON messages (Socket.IO handshake sends "0" or other non-JSON data)
//            if (!message.startsWith("{")) {
//                println("⚠️ Ignored Non-JSON Message: $message")
//                return
//            }
//
//            val json = JSONObject(message)
//            when {
//                json.has("likeUpdate") -> {
//                    val videoId = json.getString("videoId")
//                    val likesCount = json.getInt("likes")
//
//                    viewModelScope.launch {
//                        _likesFlow.value = _likesFlow.value.toMutableMap().apply {
//                            this[videoId] = likesCount
//                        }
//                    }
//                    println("👍 Like Updated: Video ID: $videoId, Likes: $likesCount")
//                }
//
//                json.has("commentUpdate") -> {
//                    val videoId = json.getString("videoId")
//                    val commentsCount = json.getInt("comments")
//
//                    viewModelScope.launch {
//                        _commentsFlow.value = _commentsFlow.value.toMutableMap().apply {
//                            this[videoId] = commentsCount
//                        }
//                    }
//                    println("💬 Comment Updated: Video ID: $videoId, Comments: $commentsCount")
//                }
//            }
//        } catch (e: Exception) {
//            println("❌ Error Parsing WebSocket Message: ${e.message}")
//        }
//    }
//
//    fun sendLike(videoId: String, userId: String, isLike: Boolean) {
//        val likeEvent = JSONObject().apply {
//            put("videoId", videoId)
//            put("userId", userId)
//            put("isLike", isLike)
//        }
//        WebSocketManager.sendMessage("42[\"likeEvent\", $likeEvent]") // ✅ Send event as an array with event name
//    }
//
//    fun sendComment(videoId: String, userId: String, commentText: String) {
//        val commentEvent = JSONObject().apply {
//            put("videoId", videoId)
//            put("userId", userId)
//            put("comment", commentText)
//        }
//        WebSocketManager.sendMessage("42[\"commentEvent\", $commentEvent]") // ✅ Send event as an array with event name
//    }
//
//
//    override fun onCleared() {
//        super.onCleared()
//        WebSocketManager.closeWebSocket()
//    }
//}

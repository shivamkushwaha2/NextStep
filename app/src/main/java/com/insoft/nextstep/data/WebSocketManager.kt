package com.insoft.nextstep.data

import okhttp3.*
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.net.URISyntaxException

object WebSocketManager {
    private var socket: Socket? = null
    private val _likesFlow = MutableStateFlow<Map<String, Int>>(emptyMap())
    val likesFlow = _likesFlow

    private val _commentsFlow = MutableStateFlow<Map<String, Int>>(emptyMap())
    val commentsFlow: StateFlow<Map<String, Int>> = _commentsFlow

    fun connectWebSocket() {
        try {
            socket = IO.socket("https://nextstepbackend.onrender.com") //  Use only base URL
            socket?.connect()

            socket?.on(Socket.EVENT_CONNECT) {
                println("WebSocket Connected")
            }

            socket?.on(Socket.EVENT_DISCONNECT) {
                println(" WebSocket Disconnected")
            }

            socket?.on("likeUpdate") { args ->
                if (args.isNotEmpty()) {
                    println("👍 Like Update: ${args[0]}")
                    val json = args[0] as JSONObject
                    val videoId = json.getString("videoId")
                    val likesCount = json.getInt("likes")

                    _likesFlow.value = _likesFlow.value.toMutableMap().apply {
                        this[videoId] = likesCount
                    }
                }
            }
            socket?.on("commentUpdate") { args ->
                if (args.isNotEmpty()) {
                    println("👍 comment Update Received: ${args[0]}")
                    val json = args[0] as JSONObject
                    val videoId = json.getString("videoId")
                    val commentsCount = json.getInt("comments")

                    _commentsFlow.value = _commentsFlow.value.toMutableMap().apply {
                        this[videoId] = commentsCount
                    }

                }
            }

        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    fun sendLike(videoId: String, userId: String, isLike: Boolean) {
        val likeEvent = JSONObject().apply {
            put("videoId", videoId)
            put("userId", userId)
            put("isLike", isLike)
        }
        socket?.emit("likeEvent", likeEvent) //Correct Socket.IO format
    }

    fun sendComment(videoId: String, userId: String, commentText: String) {
        val commentEvent = JSONObject().apply {
            put("videoId", videoId)
            put("userId", userId)
            put("comment", commentText)
        }
        socket?.emit("commentEvent", commentEvent) // correct Socket.IO format
    }

    fun closeWebSocket() {
        socket?.disconnect()
        socket = null
    }
}

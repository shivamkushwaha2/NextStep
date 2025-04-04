package com.insoft.nextstep.data

import com.insoft.nextstep.data.model.Comment
import com.insoft.nextstep.data.model.LikeInfo
import com.insoft.nextstep.data.model.Users
import okhttp3.*
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import java.net.URISyntaxException

object WebSocketManager {
    private var socket: Socket? = null


    private val _likesFlow = MutableStateFlow<Map<String, LikeInfo>>(emptyMap())
    val likesFlow: StateFlow<Map<String, LikeInfo>> = _likesFlow

    private val _commentsFlow = MutableStateFlow<Map<String, Int>>(emptyMap())
    val commentsFlow: StateFlow<Map<String, Int>> = _commentsFlow


    private val _sharesFlow = MutableStateFlow<Map<String, Int>>(emptyMap())
    val sharesFlow: StateFlow<Map<String, Int>> = _sharesFlow



    private val _likesFlowPost = MutableStateFlow<Map<String, LikeInfo>>(emptyMap())
    val likesFlowPost: StateFlow<Map<String, LikeInfo>> = _likesFlowPost


    private val _commentsMapPost = MutableStateFlow<Map<String, List<Comment>>>(emptyMap())
    val commentsMapPost: StateFlow<Map<String, List<Comment>>> = _commentsMapPost

    private val _sharesFlowPost = MutableStateFlow<Map<String, Int>>(emptyMap())
    val sharesFlowPost: StateFlow<Map<String, Int>> = _sharesFlowPost

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
                    val userLiked = json.optBoolean("userLiked", false) // ⬅️ safe optional handling

                    _likesFlow.value = _likesFlow.value.toMutableMap().apply {
                        this[videoId] = LikeInfo(likesCount, userLiked)
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
            socket?.on("shareUpdate") { args ->
                if (args.isNotEmpty()) {
                    val json = args[0] as JSONObject
                    val videoId = json.getString("videoId")
                    val sharesCount = json.getInt("shares")

                    _sharesFlow.value = _sharesFlow.value.toMutableMap().apply {
                        this[videoId] = sharesCount
                    }
                    println("🔗 Share Update Received: $videoId -> $sharesCount shares")
                }
            }



            socket?.on("postLikeUpdate") { args ->
                if (args.isNotEmpty()) {
                    val json = args[0] as JSONObject
                    val postId = json.getString("postId")
                    val likesCount = json.getInt("likes")
                    val userLiked = json.optBoolean("userLiked", false) // ⬅️ safe optional handling

                    _likesFlowPost.value = _likesFlowPost.value.toMutableMap().apply {
                        this[postId] = LikeInfo(likesCount, userLiked)
                    }
                }
            }


            socket?.on("postCommentUpdate") { args ->
                if (args.isNotEmpty()) {
                    val json = args[0] as JSONObject
                    val postId = json.getString("postId")
                    val commentJson = json.getJSONObject("comment")
                    val userJson = commentJson.getJSONObject("user")

                    val comment = Comment(
                        _id = commentJson.getString("_id"),
                        text = commentJson.getString("text"),
                        createdAt = commentJson.getString("createdAt"),
                        user = Users(
                            id = userJson.getString("_id"),
                            name = userJson.getString("name"),
                            profilePic = userJson.optString("profilePic", null)
                        )
                    )

                    _commentsMapPost.value = _commentsMapPost.value.toMutableMap().apply {
                        val current = this[postId]?.toMutableList() ?: mutableListOf()
                        if (current.none { it._id == comment._id }) {
                            current.add(comment)
                            this[postId] = current
                        }
                    }
                }
            }



            socket?.on("postShareUpdate") { args ->
                if (args.isNotEmpty()) {
                    val json = args[0] as JSONObject
                    val postId = json.getString("postId")
                    val sharesCount = json.getInt("shares")
                    println("👍 Share Update Received: ${args[0]}")

                    _sharesFlowPost.value = _sharesFlowPost.value.toMutableMap().apply {
                        this[postId] = sharesCount
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
    fun sendShare(videoId: String, userId: String) {
        val shareEvent = JSONObject().apply {
            put("videoId", videoId)
            put("userId", userId)
        }
        socket?.emit("shareEvent", shareEvent)
    }
    fun sendLike_Post(postId: String, userId: String, isLike: Boolean) {
        val likeEvent = JSONObject().apply {
            put("postId", postId)
            put("userId", userId)
            put("isLike", isLike)
        }
        socket?.emit("postLikeEvent", likeEvent) //Correct Socket.IO format
    }

    fun sendComment_Post(postId: String, userId: String, commentText: String) {
        val commentEvent = JSONObject().apply {
            put("postId", postId)
            put("userId", userId)
            put("comment", commentText)
        }
        socket?.emit("postCommentEvent", commentEvent) // correct Socket.IO format
    }
    fun sendShare_Post(postId: String, userId: String) {
        val shareEvent = JSONObject().apply {
            put("postId", postId)
            put("userId", userId)
        }
        socket?.emit("postShareEvent", shareEvent)
    }

    fun closeWebSocket() {
        socket?.disconnect()
        socket = null
    }
}

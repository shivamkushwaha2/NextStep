package com.insoft.nextstep.presentation.viewmodels

import Project
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.insoft.nextstep.data.WebSocketManager
import com.insoft.nextstep.data.model.Comment
import com.insoft.nextstep.data.model.CommentX
import com.insoft.nextstep.data.model.LikeInfo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
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

    private val _upvotesFlow = MutableStateFlow<Map<String, List<String>>>(emptyMap())
    val upvotesFlow: StateFlow<Map<String, List<String>>> = _upvotesFlow

    private val _projectommentsFlow = MutableStateFlow<Map<String, List<CommentX>>>(emptyMap())
    val projectommentsFlow: StateFlow<Map<String, List<CommentX>>> = _projectommentsFlow

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
        viewModelScope.launch {
            WebSocketManager.sharesFlow.collectLatest { updatedShares ->
                println("🔗 ViewModel Received Share Update: $updatedShares")
                _sharesFlow.value = updatedShares
            }
        }
        viewModelScope.launch {

            WebSocketManager.commentsMapPost.collect { updatedCommentsMap ->
                _commentsMapPost.value = updatedCommentsMap
            }
        }
        viewModelScope.launch {
            WebSocketManager.likesFlowPost.collectLatest { updatedLikes ->
                _likesFlowPost.value = updatedLikes
            }
        }
        viewModelScope.launch {
            WebSocketManager.sharesFlowPost.collectLatest { updatedShares ->
                _sharesFlowPost.value = updatedShares
            }
        }

        viewModelScope.launch {
            WebSocketManager.upvotesFlow.collect { updatedUpvotes ->
                _upvotesFlow.value = updatedUpvotes
            }
        }
        viewModelScope.launch {
            WebSocketManager.projectcommentsFlow.collect { updatedComments ->
                _projectommentsFlow.value = updatedComments
            }
        }

    }

        fun sendLike(videoId: String, userId: String, isLike: Boolean) {
            WebSocketManager.sendLike(videoId, userId, isLike)
        }

        fun sendComment(videoId: String, userId: String, commentText: String) {
            WebSocketManager.sendComment(videoId, userId, commentText)
        }
        fun sendShare(videoId: String, userId: String) {
            WebSocketManager.sendShare(videoId, userId)
         }

    fun sendLike_Post(postId: String, userId: String, isLike: Boolean) {
        WebSocketManager.sendLike_Post(postId, userId, isLike)
    }

    fun sendComment_Post(postId: String, userId: String, commentText: String) {
        WebSocketManager.sendComment_Post(postId, userId, commentText)
    }

    fun sendShare_Post(postId: String, userId: String) {
        WebSocketManager.sendShare_Post(postId, userId)
    }
    fun clearCommentsMap() {
        _commentsMapPost.value = emptyMap()
    }
    fun sendUpvote(projectId: String, userId: String) {
        WebSocketManager.sendProjectUpvote(projectId, userId)
    }

    fun sendProjectComment(projectId: String, userId: String, commentText: String, username: String, profilePic: String) {
        WebSocketManager.sendProjectComment(projectId, userId, commentText, username, profilePic )
    }
    override fun onCleared() {
            super.onCleared()
            WebSocketManager.closeWebSocket()
        }
}

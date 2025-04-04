package com.insoft.nextstep.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.insoft.nextstep.data.model.PostResponse
import com.insoft.nextstep.domain.usecase.CreatePostUseCase
import com.insoft.nextstep.domain.usecase.GetPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val createPostUseCase: CreatePostUseCase,
    private val getPostsUseCase: GetPostsUseCase

) : ViewModel() {

    private val _postResponse = MutableStateFlow<Response<PostResponse>?>(null)
    val postResponse: StateFlow<Response<PostResponse>?> = _postResponse.asStateFlow()

    private val _posts = MutableStateFlow<List<PostResponse>>(emptyList())
    val posts: StateFlow<List<PostResponse>> = _posts.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun createPost(token: String, content: RequestBody, image: MultipartBody.Part?) {
        viewModelScope.launch {
            try {
                val response = createPostUseCase(token, content, image)
                _postResponse.value = response
            } catch (e: Exception) {
                e.printStackTrace()
                _postResponse.value = null // Reset on error
            }
        }
    }
    fun fetchAllPosts(token: String) {
        viewModelScope.launch {
            try {
                val response = getPostsUseCase(token)
                if (response.isSuccessful) {
                    _posts.value = response.body() ?: emptyList()
                    _error.value = null
                } else {
                    _error.value = "Failed to load posts"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            }
        }
    }
}
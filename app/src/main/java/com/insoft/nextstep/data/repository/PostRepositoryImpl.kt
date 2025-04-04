package com.insoft.nextstep.data.repository

import com.insoft.nextstep.data.model.PostResponse
import com.insoft.nextstep.data.remote.ApiService
import com.insoft.nextstep.domain.repository.PostRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : PostRepository {
    override suspend fun createPost(
        token: String,
        content: RequestBody,
        image: MultipartBody.Part?
    ): Response<PostResponse> {
        return apiService.createPost(token, content, image)
    }
    override suspend fun getAllPosts(token: String): Response<List<PostResponse>> {
        return apiService.getAllPosts(token)
    }
}
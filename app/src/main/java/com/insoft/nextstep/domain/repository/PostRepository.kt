package com.insoft.nextstep.domain.repository

import com.insoft.nextstep.data.model.PostResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

interface PostRepository {
    suspend fun createPost(
        token: String,
        content: RequestBody,
        image: MultipartBody.Part?
    ): Response<PostResponse>

    suspend fun getAllPosts(token: String): Response<List<PostResponse>>

}

package com.insoft.nextstep.domain.usecase

import com.insoft.nextstep.data.model.PostResponse
import com.insoft.nextstep.domain.repository.PostRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class CreatePostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(
        token: String,
        content: RequestBody,
        image: MultipartBody.Part?
    ): Response<PostResponse> {
        return repository.createPost(token, content, image)
    }
}
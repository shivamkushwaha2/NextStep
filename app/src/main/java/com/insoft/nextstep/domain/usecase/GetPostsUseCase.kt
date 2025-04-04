package com.insoft.nextstep.domain.usecase

import com.insoft.nextstep.data.model.PostResponse
import com.insoft.nextstep.domain.repository.PostRepository
import retrofit2.Response
import javax.inject.Inject

class GetPostsUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(token: String): Response<List<PostResponse>> {
        return repository.getAllPosts(token)
    }
}

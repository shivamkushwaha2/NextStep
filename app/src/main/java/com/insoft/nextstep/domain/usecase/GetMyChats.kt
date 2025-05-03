package com.insoft.nextstep.domain.usecase

import com.insoft.nextstep.data.repository.ChatRepository
import javax.inject.Inject

class GetMyChats @Inject constructor(
    private val repo: ChatRepository
) {
    suspend operator fun invoke(token: String) = repo.getMyChats(token)
}

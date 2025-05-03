package com.insoft.nextstep.domain.usecase

import com.insoft.nextstep.data.repository.ChatRepository
import javax.inject.Inject

class SendMessage @Inject constructor(
    private val repo: ChatRepository
) {
    suspend operator fun invoke(token: String, chatId: String, text: String) =
        repo.sendMessage(token, chatId, text)
}

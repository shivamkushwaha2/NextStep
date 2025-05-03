package com.insoft.nextstep.domain.usecase

import com.insoft.nextstep.data.repository.ChatRepository
import javax.inject.Inject

class ConnectUser @Inject constructor(
    private val repo: ChatRepository
) {
    suspend operator fun invoke(token: String, targetUserId: String): String {
        return repo.connectUser(token, targetUserId)
    }
}

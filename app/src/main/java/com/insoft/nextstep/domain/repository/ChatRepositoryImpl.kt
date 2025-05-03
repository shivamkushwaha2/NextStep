package com.insoft.nextstep.domain.repository

import com.insoft.nextstep.data.model.Chat
import com.insoft.nextstep.data.model.ChatUser
import com.insoft.nextstep.data.model.Message
import com.insoft.nextstep.data.remote.ApiService
import com.insoft.nextstep.data.repository.ChatRepository
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val api: ApiService
) : ChatRepository {

    override suspend fun getMyChats(token: String): List<Chat> {
        return api.getMyChats("Bearer $token")
    }

    override suspend fun startChat(token: String, targetUserId: String): Chat {
        return api.startChat(targetUserId, "Bearer $token")
    }

    override suspend fun getMessages(token: String, chatId: String): List<Message> {
        return api.getMessages(chatId, "Bearer $token")
    }

    override suspend fun sendMessage(token: String, chatId: String, text: String): Message {
        return api.sendMessage("Bearer $token", mapOf("chat" to chatId, "text" to text))
    }

        override suspend fun connectUser(token: String, targetUserId: String): String {
            val response = api.connectUser(targetUserId, "Bearer $token")
            return response.toString()
        }
    override suspend fun getMyConnections(token: String): List<ChatUser> {
        return api.getMyConnections("Bearer $token")
    }
}

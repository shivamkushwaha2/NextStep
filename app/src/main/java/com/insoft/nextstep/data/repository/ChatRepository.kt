package com.insoft.nextstep.data.repository

import com.insoft.nextstep.data.model.Chat
import com.insoft.nextstep.data.model.ChatUser
import com.insoft.nextstep.data.model.Message

interface ChatRepository {
    suspend fun getMyChats(token: String): List<Chat>
    suspend fun startChat(token: String, targetUserId: String): Chat
    suspend fun getMessages(token: String, chatId: String): List<Message>
    suspend fun sendMessage(token: String, chatId: String, text: String): Message
    suspend fun connectUser(token: String, targetUserId: String): String
    suspend fun getMyConnections(token: String): List<ChatUser>

}

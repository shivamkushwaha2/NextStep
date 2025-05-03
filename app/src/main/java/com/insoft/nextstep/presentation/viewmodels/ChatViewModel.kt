package com.insoft.nextstep.presentation.viewmodels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.insoft.nextstep.data.model.Chat
import com.insoft.nextstep.data.model.ChatUseCases
import com.insoft.nextstep.data.model.ChatUser
import com.insoft.nextstep.data.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val useCases: ChatUseCases
) : ViewModel() {

    var chats by mutableStateOf<List<Chat>>(emptyList())
        private set

    var messages by mutableStateOf<List<Message>>(emptyList())
        private set

    var currentChatId: String? = null
    var error by mutableStateOf<String?>(null)

    fun loadChats(token: String) = viewModelScope.launch {
        try {
            chats = useCases.getMyChats(token)
        } catch (e: Exception) {
            error = e.message
        }
    }

    fun loadMessages(token: String, chatId: String) = viewModelScope.launch {
        try {
            currentChatId = chatId
            messages = useCases.getMessages(token, chatId)
        } catch (e: Exception) {
            error = e.message
        }
    }

    fun sendMessage(token: String, text: String) = viewModelScope.launch {
        currentChatId?.let {
            try {
                val msg = useCases.sendMessage(token, it, text)
                messages = messages + msg
            } catch (e: Exception) {
                error = e.message
            }
        }
    }


    var connectStatus by mutableStateOf<String?>(null)
        private set

    fun connectToUser(token: String, targetUserId: String) = viewModelScope.launch {
        try {
            val message = useCases.connectUser(token, targetUserId)
            connectStatus = message
        } catch (e: Exception) {
            connectStatus = e.message
        }
    }

    var connections by mutableStateOf<List<ChatUser>>(emptyList())
        private set

    fun loadConnections(token: String) = viewModelScope.launch {
        try {
            connections = useCases.getMyConnections(token)
        } catch (e: Exception) {
            error = e.message
        }
    }
}

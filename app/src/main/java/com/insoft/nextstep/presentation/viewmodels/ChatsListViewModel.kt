package com.insoft.nextstep.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.insoft.nextstep.data.model.ChatItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ChatsListViewModel @Inject constructor() : ViewModel() {

    private val _chatList = MutableStateFlow<List<ChatItem>>(emptyList())
    val chatList: StateFlow<List<ChatItem>> = _chatList

    fun fetchChats(currentUserId: String) {
        // Dummy data
        val dummyChats = listOf(
            ChatItem(
                connectionUserId = "user1",
                connectionUserName = "Ankit Sharma",
                connectionProfilePic = "https://randomuser.me/api/portraits/men/1.jpg",
                lastMessage = "Hey, how's the internship going?",
                timestamp = "2025-04-18T12:34:56"
            ),
            ChatItem(
                connectionUserId = "user2",
                connectionUserName = "Priya Verma",
                connectionProfilePic = "https://randomuser.me/api/portraits/women/2.jpg",
                lastMessage = "Let's review the project PR.",
                timestamp = "2025-04-18T11:20:10"
            ),
            ChatItem(
                connectionUserId = "user3",
                connectionUserName = "Rahul Yadav",
                connectionProfilePic = "https://randomuser.me/api/portraits/men/3.jpg",
                lastMessage = "I'll join the meeting soon.",
                timestamp = "2025-04-18T09:05:33"
            )
        )
        _chatList.value = dummyChats
    }
}

package com.insoft.nextstep.presentation.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class NewMessageViewModel @Inject constructor() : ViewModel() {

    private val _connections = MutableStateFlow<List<testUser>>(emptyList())
    val connections: StateFlow<List<testUser>> = _connections

    fun loadConnections(currentUserId: String) {
        // Dummy connections
        _connections.value = listOf(
            testUser(
                id = "u123",
                username = "Ananya Sharma",
                profilePic = "https://randomuser.me/api/portraits/women/1.jpg"
            ),
            testUser(
                id = "u124",
                username = "Ravi Kumar",
                profilePic = "https://randomuser.me/api/portraits/men/2.jpg"
            ),
            testUser(
                id = "u125",
                username = "Priya Singh",
                profilePic = "https://randomuser.me/api/portraits/women/3.jpg"
            ),
            testUser(
                id = "u126",
                username = "Arjun Mehta",
                profilePic = "https://randomuser.me/api/portraits/men/4.jpg"
            ),
        )
    }
}
data class testUser(
    val id: String,
    val username: String,
    val profilePic: String
)

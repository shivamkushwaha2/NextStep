package com.insoft.nextstep.presentation.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.insoft.nextstep.data.model.ChatItem
import com.insoft.nextstep.presentation.viewmodels.ChatsListViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsListScreen(
    navController: NavController,
    viewModel: ChatsListViewModel = hiltViewModel(),
    currentUserId: String
) {
    val chats by viewModel.chatList.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchChats(currentUserId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chats") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("new_message_screen") // Navigate to New Message screen
            }) {
                Icon(Icons.Default.Message, contentDescription = "New Message")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(chats.size) { pos ->
                ChatListItem(
                    chat = chats[pos],
                    currentUserId = currentUserId,
                    onClick = {
                       val chat = chats[pos]
                        navController.navigate("chat/${chat.connectionUserId}/${chat.connectionUserName}")
                    }
                )
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatListItem(
    chat: ChatItem,
    currentUserId: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = chat.connectionProfilePic,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = chat.connectionUserName,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = chat.lastMessage,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Gray
            )
        }

        Text(
            text = formatTime(chat.timestamp),
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}
@RequiresApi(Build.VERSION_CODES.O)
fun formatTime(timestamp: String): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val time = LocalDateTime.parse(timestamp)
    return time.format(formatter)
}
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ChatListItemPreview() {
    ChatListItem(
        chat = ChatItem(
            connectionUserId = "user1",
            connectionUserName = "Ankit Sharma",
            connectionProfilePic = "https://randomuser.me/api/portraits/men/1.jpg",
            lastMessage = "Let's catch up tomorrow!",
            timestamp = "2025-04-18T10:20:00"
        ),
        currentUserId = "currentUserId",
        onClick = {}
    )
}

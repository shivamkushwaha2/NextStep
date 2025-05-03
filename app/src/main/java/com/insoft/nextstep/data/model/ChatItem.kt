package com.insoft.nextstep.data.model

data class ChatItem(
    val connectionUserId: String,
    val connectionUserName: String,
    val connectionProfilePic: String,
    val lastMessage: String,
    val timestamp: String // format it into readable time
)

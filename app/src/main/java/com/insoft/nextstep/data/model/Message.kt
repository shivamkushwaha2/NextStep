package com.insoft.nextstep.data.model


//data class Message(
//    val id: String,
//    val senderId: String,
//    val receiverId: String,
//    val content: String,
//    val timestamp: String
//)

data class Message(
    val _id: String,
    val chat: String,
    val sender: User,
    val text: String,
    val createdAt: String
)
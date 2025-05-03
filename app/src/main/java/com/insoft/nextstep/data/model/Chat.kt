package com.insoft.nextstep.data.model

data class Chat(
    val _id: String,
    val participants: List<User>,
    val lastMessage: Message?
)

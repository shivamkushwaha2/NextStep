package com.insoft.nextstep.data.model

data class Users(
    val id: String,
    val name: String,
    val profilePic: String?
)

data class Comment(
    val _id: String, // Important for deduplication
    val text: String,
    val createdAt: String,
    val user: Users
)



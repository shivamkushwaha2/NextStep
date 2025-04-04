package com.insoft.nextstep.data.model

data class PostResponse(
    val _id: String,  // Post ID
    val content: String,  // Post content
    val imageUrl: String?,  // Optional image URL
    val user: User,  // User who created the post
    val likes: List<String>,  // List of users who liked the post
    val shares: List<String>,  // List of users who shared the post
    val comments: List<Comment>,  // List of comments
    val createdAt: String  // Post creation date
)

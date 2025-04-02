package com.insoft.nextstep.data.model

data class VideoResponse(
    val __v: Int,
    val _id: String,
    val comments: List<Comment>,
    val createdAt: String,
    val description: String,
    val likes: List<Any?>,
    val shares: List<Any?>,
    val user: UserInVideo,
    val videoUrl: String
)
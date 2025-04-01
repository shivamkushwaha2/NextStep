package com.insoft.nextstep.data.model


data class VideoModel(
    val _id: String,
    val videoUrl: String,
    val likes: List<String>,
    val comments: List<Comment>,
    val shares: List<String>,
    val createdAt: String
)
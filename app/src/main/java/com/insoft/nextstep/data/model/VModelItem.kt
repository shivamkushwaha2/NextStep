package com.insoft.nextstep.data.model

data class VModelItem(
    val __v: Int,
    val _id: String,
    val comments: List<Comment>,
    val createdAt: String,
    val likes: List<Any>,
    val user: Any,
    val videoUrl: String
)
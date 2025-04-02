package com.insoft.nextstep.data.model

data class SaveVideoRequest(
    val videoUrl: String,
    val userId: String,
    val description: String
)
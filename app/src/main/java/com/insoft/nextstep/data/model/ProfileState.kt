package com.insoft.nextstep.data.model

data class ProfileState(
    val user: UserX? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
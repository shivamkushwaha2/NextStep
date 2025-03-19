package com.insoft.nextstep.domain.repository

import com.insoft.nextstep.data.model.LoginRequest
import com.insoft.nextstep.data.model.LoginResponse

interface AuthRepository {
    suspend fun Login(request: LoginRequest):LoginResponse
    suspend fun SignUp(request: LoginRequest):LoginResponse

}
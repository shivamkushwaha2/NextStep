package com.insoft.nextstep.domain.repository

import com.insoft.nextstep.data.model.LoginRequest
import com.insoft.nextstep.data.model.UserModel

interface AuthRepository {
    suspend fun Login(request: LoginRequest):UserModel
    suspend fun SignUp(request: LoginRequest):UserModel

}
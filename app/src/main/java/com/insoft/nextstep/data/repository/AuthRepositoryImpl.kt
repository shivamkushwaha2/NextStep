package com.insoft.nextstep.data.repository

import com.insoft.nextstep.data.model.LoginRequest
import com.insoft.nextstep.data.model.UserModel
import com.insoft.nextstep.data.remote.ApiService
import com.insoft.nextstep.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val apiService: ApiService) : AuthRepository {

    override suspend fun Login(request: LoginRequest): UserModel {
        return apiService.Login(request)
    }

    override suspend fun SignUp(request: LoginRequest): UserModel {
      return apiService.SignUp(request)
    }
}
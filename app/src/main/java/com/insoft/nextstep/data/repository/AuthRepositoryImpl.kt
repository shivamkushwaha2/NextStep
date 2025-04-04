package com.insoft.nextstep.data.repository

import com.insoft.nextstep.data.model.LoginRequest
import com.insoft.nextstep.data.model.UserModel
import com.insoft.nextstep.data.remote.ApiService
import com.insoft.nextstep.domain.repository.AuthRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val apiService: ApiService) : AuthRepository {

    override suspend fun Login(request: LoginRequest): UserModel {
        return apiService.Login(request)
    }

    override suspend fun SignUp(
        name: RequestBody,
        email: RequestBody,
        password: RequestBody,
        profilePic: MultipartBody.Part?
    ): Response<UserModel> {
        return apiService.signup(name, email, password, profilePic)
    }

//    override suspend fun SignUp(request: LoginRequest): UserModel {
//      return apiService.SignUp(request)
//    }

}
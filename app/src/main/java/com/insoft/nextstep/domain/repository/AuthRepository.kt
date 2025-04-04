package com.insoft.nextstep.domain.repository

import com.insoft.nextstep.data.model.LoginRequest
import com.insoft.nextstep.data.model.UserModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

interface AuthRepository {
    suspend fun Login(request: LoginRequest): UserModel
//    suspend fun SignUp(request: LoginRequest):UserModel

    suspend fun SignUp(
        name: RequestBody,
        email: RequestBody,
        password: RequestBody,
        profilePic: MultipartBody.Part?
    ): Response<UserModel>

}
package com.insoft.nextstep.domain.usecase

import com.insoft.nextstep.data.model.LoginRequest
import com.insoft.nextstep.data.model.UserModel
import com.insoft.nextstep.domain.repository.AuthRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

//class SignUpUseCase @Inject constructor(private val authRepository: AuthRepository) {
//    suspend operator fun invoke(loginRequest: LoginRequest): UserModel {
//        return authRepository.SignUp(loginRequest)
//    }
//}

class SignUpUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(
        name: RequestBody,
        email: RequestBody,
        password: RequestBody,
        profilePic: MultipartBody.Part?
    ): Response<UserModel> {
        return authRepository.SignUp(name, email, password, profilePic)
    }
}

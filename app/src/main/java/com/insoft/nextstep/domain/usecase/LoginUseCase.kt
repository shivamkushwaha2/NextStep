package com.insoft.nextstep.domain.usecase

import com.insoft.nextstep.data.model.LoginRequest
import com.insoft.nextstep.data.model.LoginResponse
import com.insoft.nextstep.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val authRepository: AuthRepository)  {
   suspend operator fun invoke(request: LoginRequest): LoginResponse {
       return authRepository.Login(request)
   }
}
package com.insoft.nextstep.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.insoft.nextstep.data.model.LoginRequest
import com.insoft.nextstep.data.model.LoginResponse
import com.insoft.nextstep.domain.usecase.LoginUseCase
import com.insoft.nextstep.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val signupUseCase: SignUpUseCase
): ViewModel() {
    private val _loginState = MutableStateFlow<LoginResponse?>(null)
    val loginState: StateFlow<LoginResponse?> = _loginState

    private val _signupState = MutableStateFlow<LoginResponse?>(null)
    val signupState: StateFlow<LoginResponse?> = _signupState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    fun login(name: String, email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true  // Show progress bar
            val response = loginUseCase(LoginRequest(name, email, password))
            _loginState.value = response
            _isLoading.value = false  // Show progress bar

        }
    }

    fun signup(name: String, email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true  // Show progress bar
            val response = signupUseCase(LoginRequest(name, email, password))
            _signupState.value = response
            _isLoading.value = false  // Show progress bar

        }
    }

}
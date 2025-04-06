package com.insoft.nextstep.presentation.viewmodels

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.insoft.nextstep.data.model.LoginRequest
import com.insoft.nextstep.data.model.UserModel
import com.insoft.nextstep.domain.usecase.LoginUseCase
import com.insoft.nextstep.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val signupUseCase: SignUpUseCase
): ViewModel() {
    private val _loginState = MutableStateFlow<UserModel?>(null)
    val loginState: StateFlow<UserModel?> = _loginState

    private val _signupState = MutableStateFlow<UserModel?>(null)
    val signupState: StateFlow<UserModel?> = _signupState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun login(name: String, email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = loginUseCase(LoginRequest(name, email, password))
                _loginState.value = response
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Login Failed: ${e.message}")
                _errorMessage.value = "Login failed: ${e.localizedMessage ?: "Unknown error"}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
    fun signup(name: String, email: String, password: String, profilePicUri: Uri?, context: Context) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val namePart = name.toRequestBody("text/plain".toMediaTypeOrNull())
                val emailPart = email.toRequestBody("text/plain".toMediaTypeOrNull())
                val passwordPart = password.toRequestBody("text/plain".toMediaTypeOrNull())

                val profilePicPart = profilePicUri?.let { uri ->
                    val file = File(getPathFromUri(context, uri)!!)
                    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("profilePic", file.name, requestFile)
                }

                val response = signupUseCase(namePart, emailPart, passwordPart, profilePicPart)

                if (response.isSuccessful) {
                    _signupState.value = response.body()
                } else {
                    _errorMessage.value = response.errorBody()?.string() ?: "Signup failed"
                }

            } catch (e: Exception) {
                Log.e("AuthViewModel", "Signup Failed: ${e.message}")
                _errorMessage.value = "Signup failed: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getPathFromUri(context: Context, uri: Uri): String? {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, filePathColumn, null, null, null)

        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                return it.getString(columnIndex)
            }
        }
        return null
    }

}
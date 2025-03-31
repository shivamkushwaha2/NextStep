package com.insoft.nextstep.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.insoft.nextstep.domain.usecase.UploadVideoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UploadVideoViewModel @Inject constructor(
    private val uploadVideoUseCase: UploadVideoUseCase
) : ViewModel() {

    private val _uploadSuccess = MutableStateFlow<Boolean?>(null)
    val uploadSuccess: StateFlow<Boolean?> = _uploadSuccess

    fun uploadVideo(videoFile: File, userId: String) {
        viewModelScope.launch {
            val result = uploadVideoUseCase(videoFile, userId)
            _uploadSuccess.value = result
        }
    }
}
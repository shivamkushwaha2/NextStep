package com.insoft.nextstep.presentation.viewmodels

import Project
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.insoft.nextstep.domain.usecase.CreateProjectUseCase
import com.insoft.nextstep.domain.usecase.GetProjectsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val getProjectsUseCase: GetProjectsUseCase,
    private val createProjectUseCase: CreateProjectUseCase
) : ViewModel() {

    var projectList by mutableStateOf<List<Project>>(emptyList())
        private set

    var isLoading by mutableStateOf(true)
        private set

    var errorMessage by mutableStateOf<String?>(null)

    var selectedImageUri by mutableStateOf<Uri?>(null)

    init {
        fetchProjects()
    }

    fun fetchProjects() {
        viewModelScope.launch {
            isLoading = true
            try {
                projectList = getProjectsUseCase()
            } catch (e: Exception) {
                Log.e("ProjectViewModel", "Failed to load projects: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun createProject(token: String?, project: Project, imageFile: File?, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                createProjectUseCase(token,project, imageFile)
                onSuccess()
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }
    fun getFileFromUri(context: Context): File? {
        return selectedImageUri?.let { uri ->
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(context.cacheDir, "project_upload_${System.currentTimeMillis()}.jpg")
            file.outputStream().use { output ->
                inputStream?.copyTo(output)
            }
            file
        }
    }
}

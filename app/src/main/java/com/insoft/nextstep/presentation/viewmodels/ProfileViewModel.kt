package com.insoft.nextstep.presentation.viewmodels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.insoft.nextstep.data.model.GitHubProfile
import com.insoft.nextstep.data.model.userProfileX
import com.insoft.nextstep.domain.usecase.GetUserProfileUseCase
import com.insoft.nextstep.domain.usecase.UpdateUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.net.URL
import javax.inject.Inject
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase
) : ViewModel() {
    private val _gitHubProfile = MutableStateFlow<GitHubProfile?>(null)
    val gitHubProfile: StateFlow<GitHubProfile?> = _gitHubProfile
    private val _user = MutableStateFlow<userProfileX?>(null)
    val user: StateFlow<userProfileX?> = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    fun getGitHubProfile(username: String) {
        viewModelScope.launch {
            try {
                val url = "https://api.github.com/users/$username"
                val result = withContext(Dispatchers.IO) {
                    URL(url).readText()
                }
                val json = JSONObject(result)
                _gitHubProfile.value = GitHubProfile(
                    name = json.optString("name"),
                    bio = json.optString("bio"),
                    location = json.optString("location"),
                    blog = json.optString("blog"),
                    public_repos = json.optInt("public_repos"),
                    followers = json.optInt("followers"),
                    following = json.optInt("following"),
                    html_url = json.optString("html_url")
                )
            } catch (e: Exception) {
                Log.e("GitHubFetch", "Error fetching GitHub profile", e)
            }
        }
    }
    fun getUserProfile(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = getUserProfileUseCase(userId)
                _user.value = result
            } catch (e: Exception) {
                _error.value = e.message ?: "Something went wrong"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateUserProfile(
        token: String,
        context: Context,
        userId: String,
        firstName: String,
        lastName: String,
        bio: String,
        tags: String,
        githubUsername: String,
        profileImageUri: Uri?,
        resumeUri: Uri?
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val resolver = context.contentResolver

                fun String.toTextPart() =
                    this.toRequestBody("text/plain".toMediaTypeOrNull())

                val userIdPart = userId.toTextPart()
                val firstNamePart = firstName.toTextPart()
                val lastNamePart = lastName.toTextPart()
                val bioPart = bio.toTextPart()
                val tagsPart = tags.toTextPart()
                val githubPart = githubUsername.toTextPart()

                val profileImagePart = profileImageUri?.let { uri ->
                    resolver.openInputStream(uri)?.use {
                        val imageBytes = it.readBytes()
                        val imageRequestBody = imageBytes.toRequestBody("image/*".toMediaTypeOrNull())
                        MultipartBody.Part.createFormData("profilePic", "profile.jpg", imageRequestBody)
                    }
                }

                val resumePart = resumeUri?.let { uri ->
                    resolver.openInputStream(uri)?.use {
                        val resumeBytes = it.readBytes()
                        val resumeRequestBody = resumeBytes.toRequestBody("application/pdf".toMediaTypeOrNull())
                        MultipartBody.Part.createFormData("resume", "resume.pdf", resumeRequestBody)
                    }
                }

                val result = updateUserProfileUseCase(
                    token,
                    userIdPart,
                    firstNamePart,
                    lastNamePart,
                    bioPart,
                    tagsPart,
                    githubPart,
                    profileImagePart,
                    resumePart
                )

                _user.value = result

            } catch (e: Exception) {
                _error.value = "Failed to update profile: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

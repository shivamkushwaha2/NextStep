package com.insoft.nextstep.domain.repository

import com.insoft.nextstep.data.model.userProfileX
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface UserProfileRepository {
    suspend fun getProfile(userId: String): userProfileX
    suspend fun updateProfile(
        token: String,
        userId: RequestBody,
        firstName: RequestBody,
        lastName: RequestBody,
        bio: RequestBody,
        tags: RequestBody,
        githubUsername: RequestBody,
        profilePic: MultipartBody.Part?,
        resume: MultipartBody.Part?
    ): userProfileX
}

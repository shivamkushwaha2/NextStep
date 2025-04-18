package com.insoft.nextstep.data.repository

import com.insoft.nextstep.data.model.userProfileX
import com.insoft.nextstep.data.remote.ApiService
import com.insoft.nextstep.domain.repository.UserProfileRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject


class ProfileRepositoryImpl @Inject constructor(
    private val api: ApiService
) : UserProfileRepository {

    override suspend fun getProfile(userId: String): userProfileX {
        return api.getProfile(userId)
    }

    override suspend fun updateProfile(
        token: String ,
        userId: RequestBody,
        firstName: RequestBody,
        lastName: RequestBody,
        bio: RequestBody,
        tags: RequestBody,
        githubUsername: RequestBody,
        profilePic: MultipartBody.Part?,
        resume: MultipartBody.Part?
    ): userProfileX {
        val t = "Bearer $token"
        return api.updateProfile(t, userId, firstName,lastName, bio, tags, githubUsername, profilePic, resume)
    }
}

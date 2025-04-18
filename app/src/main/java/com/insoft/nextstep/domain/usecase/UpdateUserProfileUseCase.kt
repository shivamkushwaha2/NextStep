package com.insoft.nextstep.domain.usecase

import com.insoft.nextstep.data.model.userProfileX
import com.insoft.nextstep.domain.repository.UserProfileRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(
        token: String,
        userId: RequestBody,
        firstName: RequestBody,
        lastName: RequestBody,
        bio: RequestBody,
        tags: RequestBody,
        githubUsername: RequestBody,
        profilePic: MultipartBody.Part?,
        resume: MultipartBody.Part?
    ): userProfileX {
        return repository.updateProfile(token, userId, firstName,lastName, bio, tags, githubUsername, profilePic, resume)
    }
}

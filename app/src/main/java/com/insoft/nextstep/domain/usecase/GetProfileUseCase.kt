package com.insoft.nextstep.domain.usecase

import com.insoft.nextstep.data.model.userProfileX
import com.insoft.nextstep.domain.repository.UserProfileRepository
import okhttp3.Response
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(userId: String): userProfileX {
        return repository.getProfile(userId)
    }
}

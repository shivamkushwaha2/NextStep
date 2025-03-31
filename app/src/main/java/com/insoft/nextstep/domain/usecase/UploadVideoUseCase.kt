package com.insoft.nextstep.domain.usecase

import com.insoft.nextstep.data.remote.ApiService
import com.insoft.nextstep.domain.repository.VideoRepository
import java.io.File
import javax.inject.Inject

class UploadVideoUseCase @Inject constructor(private val repository: VideoRepository) {
    suspend operator fun invoke(videoFile: File, userId: String): Boolean {
        return repository.uploadVideo(videoFile, userId)
    }
}
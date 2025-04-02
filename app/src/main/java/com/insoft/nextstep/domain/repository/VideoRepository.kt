package com.insoft.nextstep.domain.repository

import com.insoft.nextstep.data.model.VideoModel
import com.insoft.nextstep.data.model.VideoResponse
import java.io.File


interface VideoRepository {
    suspend fun getVideos():List<VideoResponse>
    suspend fun uploadVideo(
        videoFile: File,
        userId: String,
        description: String,
    ):Boolean

}
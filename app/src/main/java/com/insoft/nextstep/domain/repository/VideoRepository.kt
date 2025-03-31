package com.insoft.nextstep.domain.repository

import com.insoft.nextstep.data.model.VideoModel
import java.io.File


interface VideoRepository {
    suspend fun getVideos():List<VideoModel>
    suspend fun uploadVideo(videoFile: File, userId: String):Boolean

}
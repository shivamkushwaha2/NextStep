package com.insoft.nextstep.data.repository

import android.util.Log
import com.insoft.nextstep.data.model.SaveVideoRequest
import com.insoft.nextstep.data.model.VideoModel
import com.insoft.nextstep.data.remote.ApiService
import com.insoft.nextstep.domain.repository.VideoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class VideoRepositoryImpl @Inject constructor(val apiService: ApiService): VideoRepository {
    override suspend fun getVideos(): List<VideoModel> {
        return apiService.getVideos()
    }

    override suspend fun uploadVideo(videoFile: File, userId: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.generatePresignedUrl()
                if (!response.isSuccessful || response.body() == null) return@withContext false

                val presignedUrl = response.body()!!.presignedUrl
                val videoUrl = response.body()!!.fileUrl

                val okHttpClient = OkHttpClient()
                val requestBody = videoFile.asRequestBody("video/mp4".toMediaTypeOrNull())
                val request = Request.Builder()
                    .url(presignedUrl)
                    .put(requestBody)
                    .build()

                val uploadResponse = okHttpClient.newCall(request).execute()
                if (!uploadResponse.isSuccessful) return@withContext false

                val saveResponse = apiService.saveVideo(SaveVideoRequest(videoUrl,"65a2bcdef1e2d34a2f89c789"))
                saveResponse.isSuccessful
            } catch (e: Exception) {
                Log.e("UploadVideo", "Upload failed: ${e.message}")
                false
            }
        }
    }
}
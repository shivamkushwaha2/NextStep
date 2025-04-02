package com.insoft.nextstep.data.remote

import com.insoft.nextstep.data.model.JobModel
import com.insoft.nextstep.data.model.LoginRequest
import com.insoft.nextstep.data.model.PresignedUrlResponse
import com.insoft.nextstep.data.model.SaveVideoRequest
import com.insoft.nextstep.data.model.UserModel
import com.insoft.nextstep.data.model.VideoModel
import com.insoft.nextstep.data.model.VideoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("api/jobs")
    suspend fun getJobs(): List<JobModel>

    @POST("user/signin")
    suspend fun Login(@Body request: LoginRequest): UserModel

    @POST("user/signup")
    suspend fun SignUp(@Body request: LoginRequest): UserModel

    @GET("videos/videos")
    suspend fun getVideos(): List<VideoResponse>

    @GET("videos/generate-presigned-url?fileType=video/mp4")
    suspend fun generatePresignedUrl(): Response<PresignedUrlResponse>


    @POST("videos/save-video")
    suspend fun saveVideo(@Body videoRequest: SaveVideoRequest): Response<VideoModel>
}
package com.insoft.nextstep.data.remote

import com.insoft.nextstep.data.model.JobModel
import com.insoft.nextstep.data.model.LoginRequest
import com.insoft.nextstep.data.model.PostResponse
import com.insoft.nextstep.data.model.PresignedUrlResponse
import com.insoft.nextstep.data.model.SaveVideoRequest
import com.insoft.nextstep.data.model.UserModel
import com.insoft.nextstep.data.model.VideoModel
import com.insoft.nextstep.data.model.VideoResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @GET("api/jobs")
    suspend fun getJobs(): List<JobModel>

    @POST("user/signin")
    suspend fun Login(@Body request: LoginRequest): UserModel

    @Multipart
    @POST("user/signup")
    suspend fun signup(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part profilePic: MultipartBody.Part?
    ): Response<UserModel>

    @GET("videos/videos")
    suspend fun getVideos(): List<VideoResponse>

    @GET("videos/generate-presigned-url?fileType=video/mp4")
    suspend fun generatePresignedUrl(): Response<PresignedUrlResponse>


    @POST("videos/save-video")
    suspend fun saveVideo(@Body videoRequest: SaveVideoRequest): Response<VideoModel>

        @Multipart
        @POST("posts/create")
        suspend fun createPost(
            @Header("Authorization") token: String,
            @Part("content") content: RequestBody,
            @Part image: MultipartBody.Part? = null
        ): Response<PostResponse>

    @GET("posts/all")
    suspend fun getAllPosts(@Header("Authorization") token: String): Response<List<PostResponse>>

}
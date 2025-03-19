package com.insoft.nextstep.data.remote

import com.insoft.nextstep.data.model.JobModel
import com.insoft.nextstep.data.model.LoginRequest
import com.insoft.nextstep.data.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("api/jobs")
    suspend fun getJobs(): List<JobModel>

    @POST("user/signin")
    suspend fun Login(@Body request: LoginRequest):LoginResponse

    @POST("user/signup")
    suspend fun SignUp(@Body request:LoginRequest): LoginResponse
}
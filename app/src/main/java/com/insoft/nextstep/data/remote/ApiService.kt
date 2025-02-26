package com.insoft.nextstep.data.remote

import com.insoft.nextstep.data.model.JobModel
import retrofit2.http.GET

interface ApiService {
    @GET("jobs")
    suspend fun getJobs(): List<JobModel>
}
package com.insoft.nextstep.data.repository

import com.insoft.nextstep.data.model.JobModel
import com.insoft.nextstep.data.remote.ApiService
import com.insoft.nextstep.domain.repository.JobRepository
import javax.inject.Inject

class JobRepositoryImp @Inject constructor(val apiService: ApiService): JobRepository {
    override suspend fun getAllJobs(): List<JobModel> {
        return apiService.getJobs()
    }

}
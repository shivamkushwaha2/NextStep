package com.insoft.nextstep.domain.repository

import com.insoft.nextstep.data.model.JobModel

interface JobRepository {
     suspend fun getAllJobs(): List<JobModel>
}
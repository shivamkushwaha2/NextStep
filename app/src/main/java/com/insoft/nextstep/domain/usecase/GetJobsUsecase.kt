package com.insoft.nextstep.domain.usecase

import com.insoft.nextstep.data.model.JobModel
import com.insoft.nextstep.domain.repository.JobRepository
import javax.inject.Inject

class GetJobsUsecase @Inject constructor(private val jobRepository: JobRepository) {
        suspend operator fun invoke(): List<JobModel> {
            return jobRepository.getAllJobs()
        }
}
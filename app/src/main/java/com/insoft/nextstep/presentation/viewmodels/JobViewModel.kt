package com.insoft.nextstep.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.insoft.nextstep.data.model.JobModel
import com.insoft.nextstep.domain.repository.JobRepository
import com.insoft.nextstep.domain.usecase.GetJobsUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobViewModel @Inject constructor( private val getJobsUseCase: GetJobsUsecase) : ViewModel() {
    private val _jobs = MutableStateFlow<List<JobModel>>(emptyList())
    val jobs: StateFlow<List<JobModel>> = _jobs
    init {
        fetchJobs()
    }

    private fun fetchJobs() {
        viewModelScope.launch {
            _jobs.value = getJobsUseCase()
        }
    }
}
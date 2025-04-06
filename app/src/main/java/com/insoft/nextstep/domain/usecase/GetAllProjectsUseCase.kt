package com.insoft.nextstep.domain.usecase

import Project
import com.insoft.nextstep.domain.repository.ProjectRepository
import javax.inject.Inject

class GetProjectsUseCase @Inject constructor(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(): List<Project> = repository.getProjects()
}

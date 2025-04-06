package com.insoft.nextstep.domain.usecase

import Project
import com.insoft.nextstep.domain.repository.ProjectRepository
import java.io.File
import javax.inject.Inject

class CreateProjectUseCase @Inject constructor(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(token: String?, project: Project, imageFile: File?): Project {
        return repository.createProject(token,project, imageFile)
    }
}

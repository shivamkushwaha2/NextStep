package com.insoft.nextstep.domain.repository

import Project
import java.io.File

interface ProjectRepository {
    suspend fun getProjects(): List<Project>
    suspend fun createProject(
        token: String?,
        project: Project,
        imageFile: File?
    ): Project}

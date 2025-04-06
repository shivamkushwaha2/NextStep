package com.insoft.nextstep.data.repository

import Project
import com.google.gson.Gson
import com.insoft.nextstep.data.remote.ApiService
import com.insoft.nextstep.domain.repository.ProjectRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class ProjectRepositoryImpl @Inject constructor(
    private val api: ApiService
) : ProjectRepository {

    override suspend fun getProjects(): List<Project> {
        return api.getProjects()
    }

    override suspend fun createProject(token: String?, project: Project, imageFile: File?): Project {
        val imagePart = imageFile?.let {
            val reqFile = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", it.name, reqFile)
        }

        fun String.toPart() = this.toRequestBody("text/plain".toMediaTypeOrNull())
        fun List<String>.toJsonArrayPart() = Gson().toJson(this).toPart()

        val response = api.createProject(
            token = "Bearer $token",
            image = imagePart,
            title = project.title.toPart(),
            description = project.description.toPart(),
            tags = project.tags.toJsonArrayPart(),
            githubLink = project.githubLink.toPart(),
            liveLink = project.liveLink.toPart(),
            techStack = project.techStack.toJsonArrayPart(),
            userId = project.postedBy.userId.toPart(),
            username = project.postedBy.username.toPart(),
            profilePic = project.postedBy.profilePic.toPart()
        )

        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception("Project creation failed: ${response.message()}")
        }
    }
}


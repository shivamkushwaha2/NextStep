package com.insoft.nextstep.data.model

data class GitHubProfile(
    val name: String?,
    val bio: String?,
    val location: String?,
    val blog: String?,
    val public_repos: Int,
    val followers: Int,
    val following: Int,
    val html_url: String
)

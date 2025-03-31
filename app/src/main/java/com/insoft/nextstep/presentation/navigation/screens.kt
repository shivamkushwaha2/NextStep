package com.insoft.nextstep.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Propane
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.vector.ImageVector
import com.insoft.nextstep.R

sealed class Screen(
    val route: String,
    val title: String? = null,
    val icon: Int? = null,
    val selectedIcon: Int? = null
) {
    object intro1 : Screen("intro_screen1")
    object intro2 : Screen("intro_screen2")
    object intro3 : Screen("intro_screen3")

    object login : Screen("login_screen")
    object signup : Screen("Signup_screen")
    object UploadVideo : Screen("uploadVideo")


    object Home : Screen("home", "Home", R.drawable.home, R.drawable.homeselected)
    object Jobs : Screen("jobs", "Jobs", R.drawable.job, R.drawable.jobselected)
    object Projects : Screen("projects", "Projects", R.drawable.project, R.drawable.projectselected)
    object Chat : Screen("chat", "Chat", R.drawable.chat, R.drawable.chatselected)
    object Videos : Screen("videos", "Shorts", R.drawable.playicon_w, R.drawable.playicon)

}

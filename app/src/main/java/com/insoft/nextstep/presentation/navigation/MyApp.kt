package com.insoft.nextstep.presentation.navigation

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.insoft.nextstep.presentation.screens.AddPostScreen
import com.insoft.nextstep.presentation.screens.CreateProjectScreen
import com.insoft.nextstep.presentation.screens.IntroScreen1
import com.insoft.nextstep.presentation.screens.IntroScreen2
import com.insoft.nextstep.presentation.screens.IntroScreen3
import com.insoft.nextstep.presentation.screens.LoginScreen
import com.insoft.nextstep.presentation.screens.SignUpScreen
import com.insoft.nextstep.presentation.screens.HomeScreen
import com.insoft.nextstep.presentation.screens.JobScreen
import com.insoft.nextstep.presentation.screens.ProjectsScreen
import com.insoft.nextstep.presentation.screens.UploadVideo
import com.insoft.nextstep.presentation.screens.VideoScreen

//
//@Composable
//fun MyApp(modifier: Modifier = Modifier) {
//    Surface(
//        modifier = modifier.fillMaxSize()
//            .windowInsetsPadding(WindowInsets.statusBars)
//    ) {
//        val navController = rememberNavController()
//        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
//
//        NavHost(
//            navController = navController,
//            startDestination = Screen.intro1.route,
//        ) {
//            composable(Screen.intro1.route) {
//                IntroScreen1(navController = navController)
//            }
//            composable(Screen.intro2.route) {
//                IntroScreen2(navController = navController)
//            }
//            composable(Screen.intro3.route) {
//                IntroScreen3(navController = navController)
//            }
//            composable(Screen.signup.route) {
//                SignUpScreen(navController = navController)
//            }
//            composable(Screen.login.route) {
//                LoginScreen(navController = navController)
//            }
//            composable(Screen.Jobs.route) {
//                JobScreen(navController = navController)
//            }
//            composable(Screen.Home.route) {
//                HomeScreen(navController = navController)
//            }
//            composable(Screen.Videos.route) {
//                VideoScreen(navController = navController,Modifier, "65a2bcdef1e2d34a2f89c789")
//            }
//            composable(Screen.Projects.route) {
//                ProjectsScreen(navController = navController)
//            }
//            composable(Screen.UploadVideo.route) {
//                UploadVideo(navController = navController)
//            }
//        }
//    }
//}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyApp(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("NextStepPrefs", Context.MODE_PRIVATE)

    val isIntroCompleted = sharedPreferences.getBoolean("isIntroCompleted", false)
    val userToken = sharedPreferences.getString("USER_TOKEN", null)
    val name = sharedPreferences.getString("USER_NAME", null)
    val id = sharedPreferences.getString("USER_ID", null)

    Log.d("MyApp", "MyApp: $isIntroCompleted  $userToken $name $id")
    val startDestination = when {
        userToken != null -> Screen.Home.route   // If user is logged in, go to Home
        isIntroCompleted -> Screen.login.route   // If intro is done, go to Login
        else -> Screen.intro1.route              // Otherwise, start intro screens
    }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = startDestination,
        ) {
            composable(Screen.intro1.route) {
                IntroScreen1(navController = navController)
            }
            composable(Screen.intro2.route) {
                IntroScreen2(navController = navController)
            }
            composable(Screen.intro3.route) {
                IntroScreen3(navController = navController)
            }
            composable(Screen.signup.route) {
                SignUpScreen(navController = navController)
            }
            composable(Screen.login.route) {
                LoginScreen(navController = navController)
            }
            composable(Screen.Jobs.route) {
                JobScreen(navController = navController)
            }
            composable(Screen.Home.route) {
                HomeScreen(navController = navController)
            }
            composable(Screen.Videos.route) {
                VideoScreen(navController = navController, Modifier)
            }
            composable(Screen.Projects.route) {
                ProjectsScreen(navController = navController)
            }
            composable(Screen.UploadVideo.route) {
                UploadVideo(navController = navController)
            }
            composable("add_post") { AddPostScreen(navController) }
            composable("create_project") { CreateProjectScreen(navController) }
        }
    }
}

package com.insoft.nextstep.presentation.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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

@Composable
fun MyApp(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxSize()
         .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        val navController = rememberNavController()
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

//        val showBottomBar = currentRoute in listOf(
//            Screen.Home.route,
//            Screen.Jobs.route,
//            Screen.Projects.route,
//            Screen.Chat.route,
//            Screen.Profile.route
//        )
//        Scaffold(
//            bottomBar = {
//                if (showBottomBar) BottomNavigationBar(navController)
//            }
//        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
//                modifier = Modifier.padding(paddingValues)
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
                    VideoScreen(navController = navController,Modifier, "65a2bcdef1e2d34a2f89c789")
                }
                composable(Screen.Projects.route) {
                    ProjectsScreen(navController = navController)
                }
                composable(Screen.UploadVideo.route) {
                    UploadVideo(navController = navController)
                }
            }
//        }
    }
}
package com.insoft.nextstep.presentation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.insoft.nextstep.presentation.navigation.Routes
import com.insoft.nextstep.presentation.screens.IntroScreen1
import com.insoft.nextstep.presentation.screens.IntroScreen2
import com.insoft.nextstep.presentation.screens.IntroScreen3
import com.insoft.nextstep.presentation.screens.LoginScreen
import com.insoft.nextstep.presentation.screens.SignUpScreen
import com.insoft.nextstep.presentation.screens.HomeScreen

@Composable
fun MyApp(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Routes.intro1) {
            composable(Routes.intro1) {
                IntroScreen1(navController = navController)
            }
            composable(Routes.intro2) {
                IntroScreen2(navController = navController)
            }
            composable(Routes.intro3) {
                IntroScreen3(navController = navController)
            }
            composable(Routes.signup) {
                SignUpScreen(navController = navController)
            }
            composable(Routes.login) {
                LoginScreen(navController = navController)
            }
            composable(Routes.homescreen) {
             HomeScreen(navController = navController)
            }
        }
    }
}
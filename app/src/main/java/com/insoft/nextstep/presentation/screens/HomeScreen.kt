package com.insoft.nextstep.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.insoft.nextstep.presentation.components.HeadingText

@Composable
fun HomeScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    HeadingText("HomeScreen")
}
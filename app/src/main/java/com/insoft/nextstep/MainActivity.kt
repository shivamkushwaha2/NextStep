package com.insoft.nextstep

import android.graphics.Color.TRANSPARENT
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.insoft.nextstep.presentation.navigation.MyApp
import com.insoft.nextstep.ui.theme.NextStepTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(SystemBarStyle.light(TRANSPARENT, TRANSPARENT))
        setContent {
            NextStepTheme {
                MyApp()
            }
        }
    }
}


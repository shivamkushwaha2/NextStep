package com.insoft.nextstep.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.insoft.nextstep.R
import com.insoft.nextstep.presentation.components.PageIndicator
import com.insoft.nextstep.presentation.navigation.Screen

@Composable
fun IntroScreen2(modifier: Modifier = Modifier, navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        colorResource(R.color.blue_gradient_color)  // Blue shade
                    )
                )
            )
    ) {

        Image(
            painter = painterResource(R.drawable.intro_img2),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(start = 10.dp)
                .graphicsLayer(scaleX = 1.4f, scaleY = 1.2f)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 40.dp), Arrangement.Bottom
        ) {
            Text(
                text = "Build, Showcase, and Connect",
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Start,
                style = TextStyle(
                    fontSize = 24.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "Create a strong profile, showcase your projects, and connect with peers. Join groups, participate in discussions, and collaborate to enhance your academic and professional growth.",
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                textAlign = TextAlign.Start,
                style = TextStyle(
                    fontSize = 18.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Normal
                )
            )
            Spacer(Modifier.height(50.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
                Arrangement.SpaceBetween,
                Alignment.CenterVertically
            ) {
                PageIndicator(3,1)

                CircularIconsRow(
                    onBackClick = { navController.navigateUp() },
                    onForwardClick = { navController.navigate(Screen.intro3.route) }
                )
            }
            Spacer(Modifier.height(50.dp))

        }

    }
}

@Preview(showSystemUi = true)
@Composable
private fun IntroPreview() {
    IntroScreen2(Modifier, rememberNavController())
}
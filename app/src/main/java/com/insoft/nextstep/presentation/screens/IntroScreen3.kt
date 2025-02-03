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
import com.insoft.nextstep.presentation.navigation.Routes

@Composable
fun IntroScreen3(modifier: Modifier = Modifier, navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        colorResource(R.color.purple_gradient_color)  // Blue shade
                    )
                )
            )
    ) {

        Image(
            painter = painterResource(R.drawable.intro_img3),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp).graphicsLayer(scaleX = 1.2f, scaleY = 1.2f),
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 40.dp), Arrangement.Bottom
        ) {
            Text(
                text = "Learn, Share, and Achieve",
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
                text = "Access and share notes, enroll in courses, and find scholarships to support your education. Stay informed and make the most of your learning experience.",
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
                modifier = Modifier.fillMaxWidth(),
                Arrangement.SpaceBetween,
                Alignment.CenterVertically
            ) {
                Text(
                    text = "-------",
                    modifier = modifier.padding(16.dp),
                    textAlign = TextAlign.Start,
                    style = TextStyle(
                        fontSize = 28.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                )
                CircularIconsRow(
                    onBackClick = { navController.navigateUp() },
                    onForwardClick = {
                        navController.navigate(Routes.signup) {
                            popUpTo(Routes.intro1) { inclusive = true }
                        }
                    }
                )
            }
            Spacer(Modifier.height(50.dp))

        }

    }
}

@Preview(showSystemUi = true)
@Composable
private fun IntroPreview() {
    IntroScreen3(Modifier, rememberNavController())
}
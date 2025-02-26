package com.insoft.nextstep.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.insoft.nextstep.ui.theme.StatusBarColor

@Composable
fun IntroScreen1(modifier: Modifier = Modifier, navController: NavHostController) {
StatusBarColor(Color.Blue)
    Box(modifier = Modifier.fillMaxSize().background(
        brush = Brush.verticalGradient(
            colors = listOf(
                Color.White,
                colorResource(R.color.green_gradient_color)
            )
        )
    )) {

        Image(
            painter = painterResource(R.drawable.intro_img1),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp),
        )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 40.dp), Arrangement.Bottom
            ) {
                Text(
                    text = "Empower Your Career Journey",
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
                    text = "NextStep helps students explore jobs, internships, and professional opportunities with ease. Find career options that match your skills and take the next step toward success.",
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
                    PageIndicator(3,0)
                    CircularIconsRow(
                        onBackClick = {  },
                        onForwardClick = { navController.navigate(Screen.intro2.route) }
                    )
                }
                Spacer(Modifier.height(50.dp))

            }

    }
}
@Composable
fun CircularIconsRow(
    onBackClick: () -> Unit,
    onForwardClick: () -> Unit
) {
    Row(
        modifier = Modifier.padding(end = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = Color.Black,
            modifier = Modifier
                .size(54.dp)
                .background(color = colorResource(R.color.white), CircleShape)
                .clip(CircleShape)
                .padding(8.dp)
                .clickable {
                    onBackClick()
                }
        )
        Spacer(modifier = Modifier.width(16.dp))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "Forward",
            tint = Color.White,
            modifier = Modifier
                .size(54.dp)
                .background(Color.Black, CircleShape)
                .clip(CircleShape)
                .padding(8.dp)
                .clickable {
                    onForwardClick()
                }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun IntroPreview() {
    IntroScreen1(Modifier, rememberNavController())
}
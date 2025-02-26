package com.insoft.nextstep.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.insoft.nextstep.R
import com.insoft.nextstep.presentation.components.ButtonComponent
import com.insoft.nextstep.presentation.components.ClickableSignUpTextComponent
import com.insoft.nextstep.presentation.components.DividerTextComponent
import com.insoft.nextstep.presentation.components.HeadingText
import com.insoft.nextstep.presentation.components.InputBox
import com.insoft.nextstep.presentation.components.PasswordInputBox
import com.insoft.nextstep.presentation.navigation.Screen
import com.insoft.nextstep.ui.theme.StatusBarColor

@Composable
fun LoginScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
//            .background(
//                brush = Brush.verticalGradient(
//                    listOf(
//                        colorResource(R.color.white), colorResource(R.color.green_gradient_color)
//                    )
//                )
//            )
    ) {
//        Image(
//            painter = painterResource(R.drawable.bg_image),
//            contentDescription = null,
//            contentScale = ContentScale.FillHeight,
//            modifier = Modifier.fillMaxSize()
//        )
        Card(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, bottom = 80.dp)
                .align(alignment = Alignment.BottomCenter),

            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                colorResource(R.color.blue_gradient_color),
                                colorResource(R.color.white)
                            )
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .background(Color.Transparent)
                ) {

//                    NormalText("Hey There,")
                    Spacer(modifier = Modifier.height(30.dp))

                    HeadingText("Welcome Back")

                    Spacer(modifier = Modifier.height(30.dp))

                    InputBox(
                        "Email",
                        Modifier.background(Color.White),
                        painterResource = painterResource(id = R.drawable.baseline_email_24)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    PasswordInputBox(
                        "Password",
                        painterResource = painterResource(id = R.drawable.baseline_lock_24)
                    )
//            Text(
//                text = "Forgot Password?",
//                style = TextStyle(
//                    fontSize = 18.sp,
//                    color = Color.Gray,
//                    fontWeight = FontWeight.Medium,
//                    textAlign = TextAlign.Center
//                ),
//                modifier = Modifier
//                    .padding(top = 25.dp)
//                    .fillMaxWidth()
//            )
                    Spacer(modifier = Modifier.height(40.dp))
                    ButtonComponent(
                        text = "Login", modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate(Screen.Home.route) }
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    DividerTextComponent()

                    Spacer(modifier = Modifier.height(20.dp))
                    ClickableSignUpTextComponent(navController)
                    Spacer(modifier = Modifier.height(20.dp))

                }
            }

        }


    }
}


@Preview(showSystemUi = true)
@Composable
private fun LoginPreview() {
    LoginScreen(navController = rememberNavController())
}
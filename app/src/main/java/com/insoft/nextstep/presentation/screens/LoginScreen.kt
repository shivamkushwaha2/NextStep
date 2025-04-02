package com.insoft.nextstep.presentation.screens

import android.content.Context
import android.util.Log
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.insoft.nextstep.presentation.viewmodels.AuthViewModel
import com.insoft.nextstep.ui.theme.StatusBarColor

@Composable
fun LoginScreen(navController: NavHostController, viewModel: AuthViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("NextStepPrefs", Context.MODE_PRIVATE)
    val savedEmail = sharedPreferences.getString("USER_EMAIL", "") ?: ""

    val loginState by viewModel.loginState.collectAsState()
    var email by remember { mutableStateOf(savedEmail) }
    var password by remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading.collectAsState()

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
                        painterResource = painterResource(id = R.drawable.baseline_email_24),
                        value = email,
                        onValueChange = { email = it }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    PasswordInputBox(
                        "Password",
                        painterResource = painterResource(id = R.drawable.baseline_lock_24),
                        value = password,
                        onValueChange = { password = it }
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

                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally)   )
                    } else {
                        ButtonComponent(
                            text = "Login",
                            modifier = Modifier.fillMaxWidth().clickable {
                                viewModel.login("", email, password)
                            }
                        )
                    }
//                    ButtonComponent(
//                        text = "Login", modifier = Modifier
//                            .fillMaxWidth()
//                            .clickable {
//                                viewModel.login("",email, password)
//                                loginState?.let {
//                                    Log.d("SignUpScreen", "success")
//                                    navController.navigate(Screen.Home.route)
//                                }
//                            }
//                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    DividerTextComponent()

                    Spacer(modifier = Modifier.height(20.dp))
                    ClickableSignUpTextComponent(navController)
                    Spacer(modifier = Modifier.height(20.dp))

                }
            }

        }

        LaunchedEffect(loginState) {
            loginState?.let {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.login.route) { inclusive = true }  // Clears backstack
                }
            }
        }

    }


    LaunchedEffect(loginState) {
        loginState?.let { user ->
            saveUserData(context, email, user.token, user.user.name,user.user._id)
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.login.route) { inclusive = true }
            }
        }
    }
}

private fun saveUserData(context: Context, email: String, token: String,name:String,id:String) {
    val sharedPreferences = context.getSharedPreferences("NextStepPrefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().apply {
        putString("USER_EMAIL", email)
        putString("USER_TOKEN", token)
        putString("USER_ID", id)
        putString("USER_NAME", name)
        apply()
    }
}
@Preview(showSystemUi = true)
@Composable
private fun LoginPreview() {
    LoginScreen(navController = rememberNavController())
}
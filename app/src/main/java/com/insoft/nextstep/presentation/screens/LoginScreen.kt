package com.insoft.nextstep.presentation.screens

import android.content.Context
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        colorResource(R.color.green_gradient_color),  colorResource(R.color.white)
                    )
                )
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_image),
            contentDescription = "Login Background",
            modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        Card(
            modifier = Modifier
                .padding(start = 5.dp, end = 5.dp, bottom = 70.dp)
                .align(alignment = Alignment.BottomCenter),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(14.dp)
        ) {
            Box(
                modifier = Modifier.background(
                    brush = Brush.verticalGradient(
                        listOf(
                            colorResource(R.color.blue_gradient_color),
                            colorResource(R.color.white)
                        )
                    )
                )
            ) {
                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Spacer(modifier = Modifier.height(30.dp))
                    HeadingText("Welcome Back")
                    Spacer(modifier = Modifier.height(30.dp))

                    InputBox(
                        label = "Email",
                        painterResource = painterResource(id = R.drawable.baseline_email_24),
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = null
                        },
                        isError = emailError != null
                    )
                    emailError?.let {
                        Text(
                            text = it,
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    PasswordInputBox(
                        label = "Password",
                        painterResource = painterResource(id = R.drawable.baseline_lock_24),
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = null
                        },
                        isError = passwordError != null
                    )
                    passwordError?.let {
                        Text(
                            text = it,
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    } else {
                        ButtonComponent(
                            text = "Login",
                            modifier = Modifier.fillMaxWidth().clickable {
                                var valid = true
                                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                    emailError = "Invalid email format"
                                    valid = false
                                }
                                if (password.length < 6) {
                                    passwordError = "Password must be at least 6 characters"
                                    valid = false
                                }
                                if (valid) {
                                    viewModel.login("", email, password)
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    DividerTextComponent()
                    Spacer(modifier = Modifier.height(20.dp))
                    ClickableSignUpTextComponent(navController)
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }

        // Navigation on successful login
        LaunchedEffect(loginState) {
            loginState?.let { user ->
                saveUserData(context, email, user.token, user.user.name, user.user._id,user.user.profilePic)
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.login.route) { inclusive = true }
                }
            }
        }

        LaunchedEffect(errorMessage) {
            errorMessage?.let { msg ->
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }
    }
}

private fun saveUserData(context: Context, email: String, token: String,name:String,id:String,profilePic:String) {
    Log.d("LoginScreen", "Saving user data: $email, $token, $name, $id, $profilePic")
    val sharedPreferences = context.getSharedPreferences("NextStepPrefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().apply {
        putString("USER_EMAIL", email)
        putString("USER_TOKEN", token)
        putString("USER_ID", id)
        putString("USER_NAME", name)
        putString("USER_IMAGE", profilePic)
        apply()
    }
}
@Preview(showSystemUi = true)
@Composable
private fun LoginPreview() {
    LoginScreen(navController = rememberNavController())
}
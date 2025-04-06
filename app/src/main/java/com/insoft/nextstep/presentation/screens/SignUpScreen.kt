package com.insoft.nextstep.presentation.screens

import android.content.Context
import android.net.Uri
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
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
import com.insoft.nextstep.presentation.components.ClickableLoginTextComponent
import com.insoft.nextstep.presentation.components.DividerTextComponent
import com.insoft.nextstep.presentation.components.HeadingText
import com.insoft.nextstep.presentation.components.InputBox
import com.insoft.nextstep.presentation.components.PasswordInputBox
import com.insoft.nextstep.presentation.navigation.Screen
import com.insoft.nextstep.presentation.viewmodels.AuthViewModel
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter

@Composable
fun SignUpScreen(navController: NavHostController, viewModel: AuthViewModel = hiltViewModel()) {
    val signupState by viewModel.signupState.collectAsState()
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var profilePicUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val isLoading by viewModel.isLoading.collectAsState()

    // Validation error states
    var firstNameError by remember { mutableStateOf<String?>(null) }
    var lastNameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    val errorMessage by viewModel.errorMessage.collectAsState()

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        profilePicUri = uri
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        colorResource(R.color.white) ,colorResource(R.color.green_gradient_color),
                    )
                )
            )
    ) {
//        Image(
//            painter = painterResource(id = R.drawable.app_image),
//            contentDescription = "Login Background",
//            modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth(),
//            contentScale = ContentScale.FillWidth
//        )
        Card(
            modifier = Modifier
                .padding(start = 5.dp, end = 5.dp, bottom = 90.dp, top = 5.dp)
                .align(Alignment.BottomCenter),
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
                    Spacer(modifier = Modifier.height(10.dp))
                    HeadingText("Create An Account")
                    Spacer(modifier = Modifier.height(20.dp))

                    // Profile Image Picker
                    val imagePainter: Painter = if (profilePicUri != null) {
                        rememberAsyncImagePainter(model = profilePicUri)
                    } else {
                        painterResource(id = R.drawable.profile)
                    }
                    Image(
                        painter = imagePainter,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .clickable { launcher.launch("image/*") }
                            .align(Alignment.CenterHorizontally)
                            .size(70.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Gray, CircleShape)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    InputBox(
                        label = "First Name",
                        painterResource = painterResource(id = R.drawable.baseline_person_24),
                        value = firstName,
                        onValueChange = {
                            firstName = it
                            firstNameError = null
                        },
                        isError = firstNameError != null
                    )
                    if (firstNameError != null) {
                        Text(firstNameError!!, color = Color.Red, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    InputBox(
                        label = "Last Name",
                        painterResource = painterResource(id = R.drawable.baseline_person_24),
                        value = lastName,
                        onValueChange = {
                            lastName = it
                            lastNameError = null
                        },
                        isError = lastNameError != null
                    )
                    if (lastNameError != null) {
                        Text(lastNameError!!, color = Color.Red, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

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
                    if (emailError != null) {
                        Text(emailError!!, color = Color.Red, fontSize = 12.sp)
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
                    if (passwordError != null) {
                        Text(passwordError!!, color = Color.Red, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    } else {
                        ButtonComponent(
                            text = "Sign Up",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    var valid = true

                                    if (firstName.isBlank()) {
                                        Toast.makeText(context, "First name cannot be empty", Toast.LENGTH_SHORT).show()
                                        valid = false
                                    }

                                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                        Toast.makeText(context, "Invalid email format", Toast.LENGTH_SHORT).show()
                                        valid = false
                                    }

                                    if (password.length < 6) {
                                        Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                                        valid = false
                                    }

                                    if (profilePicUri == null) {
                                        Toast.makeText(context, "Please select a profile picture", Toast.LENGTH_SHORT).show()
                                        valid = false
                                    }

                                    if (valid) {
                                        viewModel.signup(
                                            "$firstName $lastName",
                                            email,
                                            password,
                                            profilePicUri,
                                            context
                                        )
                                    }
                                }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    DividerTextComponent(Modifier)
                    Spacer(modifier = Modifier.height(20.dp))
                    ClickableLoginTextComponent(navController = navController)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }

    LaunchedEffect(signupState) {
        signupState?.let { user ->
            saveUserData(
                context = context,
                email = email,
                token = user.token,
                name = user.user.name,
                id = user.user._id,
                profilePic = user.user.profilePic
            )
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.signup.route) { inclusive = true }
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


@Composable
fun ErrorText(message: String) {
    Text(
        text = message,
        color = Color.Red,
        fontSize = 14.sp,
        modifier = Modifier.padding(start = 4.dp, top = 2.dp)
    )
}

private fun saveUserData(context: Context, email: String, token: String, name: String, id: String, profilePic: String) {
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

@Preview(showBackground = true)
@Composable
private fun SignUpPreview() {
    SignUpScreen(navController = rememberNavController())
}

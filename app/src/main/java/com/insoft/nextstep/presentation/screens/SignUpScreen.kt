package com.insoft.nextstep.presentation.screens

import android.content.Context
import android.util.Log
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
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current
    var profilePicUri by remember { mutableStateOf<android.net.Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        profilePicUri = uri
    }
    Log.d("TAG", "SignUpScreen: $profilePicUri")
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

//        if (profilePicUri != null) {
//            Image(
//                painter = rememberAsyncImagePainter(profilePicUri),
//                contentDescription = "Profile Picture",
//                modifier = Modifier
//                    .size(100.dp)
//                    .clip(CircleShape)
//                    .border(2.dp, Color.Gray, CircleShape)
//            )
//        }
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
                    Spacer(modifier = Modifier.height(10.dp))
                    HeadingText("Create An Account")
                    Spacer(modifier = Modifier.height(20.dp))

                    val imagePainter: Painter = if (profilePicUri != null) {
                        rememberAsyncImagePainter(model = profilePicUri)
                    } else {
                        painterResource(id = R.drawable.profile) // Your default drawable
                    }
                    Image(
                        painter = imagePainter,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .clickable {
                                launcher.launch("image/*")
                            }
                            .align(Alignment.CenterHorizontally)
                            .size(70.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Gray, CircleShape)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    InputBox(
                        "First Name",
                        Modifier,
                        painterResource(id = R.drawable.baseline_person_24),
                        value = firstName,
                        onValueChange = { firstName = it }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    InputBox(
                        "Last Name",
                        painterResource = painterResource(id = R.drawable.baseline_person_24),
                        value = lastName,
                        onValueChange = { lastName = it }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    InputBox(
                        "Email",
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
//            Spacer(modifier = Modifier.height(5.dp))

//            CheckBoxComponent(
//                "I agree to the Terms & Conditions",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = 16.dp)
//            )
                    Spacer(modifier = Modifier.height(30.dp))
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    } else {

                        ButtonComponent(
                            text = "Sign Up",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.signup(
                                        "$firstName $lastName",
                                        email,
                                        password,
                                        profilePicUri,
                                        context
                                    )
                                    Log.d("SignUpScreen", "success")
                                }
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    DividerTextComponent(Modifier)

                    Spacer(modifier = Modifier.height(20.dp))

                    ClickableLoginTextComponent(navController = navController)
                    Spacer(modifier = Modifier.height(20.dp))


                }
            }
        }
    }

    LaunchedEffect(signupState) {
        signupState?.let { user ->
            saveUserData(context, email, user.token, user.user.name, user.user._id,user.user.profilePic)
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.signup.route) { inclusive = true }
            }
        }
    }


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

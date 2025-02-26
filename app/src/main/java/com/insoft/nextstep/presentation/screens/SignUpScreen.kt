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
import com.insoft.nextstep.presentation.components.ClickableLoginTextComponent
import com.insoft.nextstep.presentation.components.DividerTextComponent
import com.insoft.nextstep.presentation.components.HeadingText
import com.insoft.nextstep.presentation.components.InputBox
import com.insoft.nextstep.presentation.components.PasswordInputBox
import com.insoft.nextstep.presentation.navigation.Screen

@Composable
fun SignUpScreen(navController: NavHostController) {

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
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
//            NormalText("Hey There,")
                    Spacer(modifier = Modifier.height(30.dp))
                    HeadingText("Create An Account")
                    Spacer(modifier = Modifier.height(30.dp))
                    InputBox(
                        "First Name",
                        Modifier,
                        painterResource(id = R.drawable.baseline_person_24)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    InputBox(
                        "Last Name",
                        painterResource = painterResource(id = R.drawable.baseline_person_24)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    InputBox(
                        "Email",
                        painterResource = painterResource(id = R.drawable.baseline_email_24)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    PasswordInputBox(
                        "Password",
                        painterResource = painterResource(id = R.drawable.baseline_lock_24)
                    )
//            Spacer(modifier = Modifier.height(5.dp))

//            CheckBoxComponent(
//                "I agree to the Terms & Conditions",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = 16.dp)
//            )
                    Spacer(modifier = Modifier.height(30.dp))

                    ButtonComponent(
                        text = "Sign Up",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate(Screen.Home.route) }
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    DividerTextComponent(Modifier)

                    Spacer(modifier = Modifier.height(20.dp))

                    ClickableLoginTextComponent(navController = navController)
                    Spacer(modifier = Modifier.height(20.dp))


                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun SignUpPreview() {
    SignUpScreen(navController = rememberNavController())
}

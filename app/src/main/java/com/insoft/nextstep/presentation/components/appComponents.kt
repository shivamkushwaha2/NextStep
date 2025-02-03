package com.insoft.nextstep.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.insoft.nextstep.R
import com.insoft.nextstep.presentation.navigation.Routes


@Composable
fun NormalText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        style = TextStyle(
            fontSize = 18.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium
        )
    )
}

@Composable
fun HeadingText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        style = TextStyle(
            fontSize = 24.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
    )
}

@Composable
fun InputBox(label: String, modifier: Modifier = Modifier, painterResource: Painter) {
    val txt = remember { mutableStateOf("") }

    OutlinedTextField(
        value = txt.value,
        label = {
            Text(
                text = label,
                color = Color.DarkGray,
                fontSize = 16.sp,
                modifier = Modifier.background(Color.Transparent)
            )
        },
        textStyle = TextStyle(fontSize = 16.sp),
        keyboardOptions = KeyboardOptions.Default,
        onValueChange = { txt.value = it },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        leadingIcon = {
            Icon(
                painter = painterResource,
                contentDescription = ""
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0x1DF4F6F6),
            unfocusedContainerColor = Color(0xFFF6F6F6),
        )
    )
}

@Composable
fun PasswordInputBox(label: String, modifier: Modifier = Modifier, painterResource: Painter) {
    val password = remember { mutableStateOf("") }
    val isVisible = remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password.value,
        onValueChange = { password.value = it },
        modifier = modifier.fillMaxWidth(),
        label = {
            Text(
                text = label,
                color = Color.DarkGray,
                fontSize = 16.sp,
                modifier = Modifier.background(Color.Transparent)
            )
        },
        textStyle = TextStyle(fontSize = 16.sp),
        shape = RoundedCornerShape(16.dp),
        leadingIcon = {
            Icon(
                painter = painterResource,
                contentDescription = null // Provide a description if needed
            )
        },
        trailingIcon = {
            val icon = if (isVisible.value) {
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }
            val description = if (isVisible.value) "Hide password" else "Show password"
            IconButton(onClick = { isVisible.value = !isVisible.value }) {
                Icon(imageVector = icon, contentDescription = description)
            }
        },
        visualTransformation = if (isVisible.value) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0x1DF4F6F6),
            unfocusedContainerColor = Color(0xFFF6F6F6),
        )
    )
}

@Composable
fun CheckBoxComponent(text: String, modifier: Modifier) {
    val isChecked = remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Checkbox(isChecked.value, onCheckedChange = {
            isChecked.value = !isChecked.value
        })
        Text(
            text = text,
            style = TextStyle(
                fontSize = 16.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Start
            ),
            modifier = Modifier
                .padding(2.dp)
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun ButtonComponent(text: String, modifier: Modifier = Modifier) {
    val txt = remember { mutableStateOf(text) }
    Button(
        onClick = {

        },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
    ) {
        Box(
            modifier
                .fillMaxWidth()
                .heightIn(48.dp)
                .background(
                    Brush.linearGradient(
                        listOf(
                            colorResource(R.color.blue1),
                            colorResource(R.color.blue2)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = txt.value,
                style = TextStyle(
                    fontSize = 18.sp,
                    color = Color.White,
                )
            )
        }
    }
}

@Composable
fun DividerTextComponent(modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        HorizontalDivider(
            modifier.weight(1f),
            color = Color.LightGray,
            thickness = 1.dp
        )
        Text(
            text = "or",
            modifier.padding(start = 8.dp, end = 8.dp),
            color = Color.Black
        )
        HorizontalDivider(
            modifier.weight(1f),
            color = Color.LightGray,
            thickness = 1.dp
        )
    }

}
@Composable
fun ClickableLoginTextComponent(navController: NavHostController?) {
    val annotatedString = buildAnnotatedString {
        append("Already have an account? ")
        pushStringAnnotation(tag = "Login", annotation = "Login")
        withStyle(style = SpanStyle(color = colorResource(R.color.blue1), fontSize = 18.sp)) {
            append("Login")
        }
        pop()
    }

    androidx.compose.foundation.text.ClickableText(
        text = annotatedString,
        modifier = Modifier.fillMaxWidth(),
        style = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            textAlign = TextAlign.Center
        ),
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "Login", start = offset, end = offset)
                .firstOrNull()?.let {
                    navController?.navigate(Routes.login){
                        popUpTo(Routes.signup){
                            inclusive = true
                        }
                    }
                }
        }
    )
}

@Composable
fun ClickableSignUpTextComponent(navController: NavHostController?) {
    val annotatedString = buildAnnotatedString {
        append("Don't have an account? ")
        pushStringAnnotation(tag = "SignUp", annotation = "SignUp")
        withStyle(style = SpanStyle(color = colorResource(R.color.blue1), fontSize = 18.sp)) {
            append("SignUp")
        }
        pop()
    }

    androidx.compose.foundation.text.ClickableText(
        text = annotatedString,
        modifier = Modifier.fillMaxWidth(),
        style = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            textAlign = TextAlign.Center
        ),
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "SignUp", start = offset, end = offset)
                .firstOrNull()?.let {
                    navController?.navigate(Routes.signup){
                        popUpTo(Routes.login){
                            inclusive = true
                        }
                    }
                }
        }
    )
}


@Preview(showSystemUi = true)
@Composable
private fun default() {
    Surface(Modifier.windowInsetsPadding(WindowInsets.statusBars)) {
        ClickableLoginTextComponent(rememberNavController())
//        DividerTextComponent(Modifier)
//        ButtonComponent("Sign Up", Modifier)
//        PasswordInputBox("Fullname", Modifier, painterResource(id = R.drawable.baseline_person_24))
    }
}
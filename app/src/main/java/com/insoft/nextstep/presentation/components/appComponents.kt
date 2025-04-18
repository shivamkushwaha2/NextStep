package com.insoft.nextstep.presentation.components

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.filled.KeyboardDoubleArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.insoft.nextstep.R
import com.insoft.nextstep.data.model.JobModel
import com.insoft.nextstep.data.model.PostResponse
import com.insoft.nextstep.presentation.navigation.Screen
import com.insoft.nextstep.presentation.viewmodels.WebSocketViewModel
import com.insoft.nextstep.ui.theme.Blue1
import com.insoft.nextstep.ui.theme.Blue2
import com.insoft.nextstep.ui.theme.Purple40
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.time.Duration


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
fun InputBox(
    label: String,
    modifier: Modifier = Modifier,
    painterResource: Painter,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = value,
        label = { Text(text = label, color = Color.DarkGray, fontSize = 16.sp) },
        textStyle = TextStyle(fontSize = 16.sp),
        keyboardOptions = KeyboardOptions.Default,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        isError = isError,
        leadingIcon = { Icon(painter = painterResource, contentDescription = "") },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0x1DF4F6F6),
            unfocusedContainerColor = Color(0xFFF6F6F6),
        )
    )
}
@Composable
fun PasswordInputBox(
    label: String,
    modifier: Modifier = Modifier,
    painterResource: Painter,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false
) {
    val isVisible = remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text(text = label, color = Color.DarkGray, fontSize = 16.sp) },
        textStyle = TextStyle(fontSize = 16.sp),
        shape = RoundedCornerShape(16.dp),
        leadingIcon = { Icon(painter = painterResource, contentDescription = null) },
        trailingIcon = {
            val icon = if (isVisible.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            val description = if (isVisible.value) "Hide password" else "Show password"
            IconButton(onClick = { isVisible.value = !isVisible.value }) {
                Icon(imageVector = icon, contentDescription = description)
            }
        },
        visualTransformation = if (isVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        isError = isError,
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
                    navController?.navigate(Screen.login.route) {
                        popUpTo(Screen.signup.route) {
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
                    navController?.navigate(Screen.signup.route) {
                        popUpTo(Screen.login.route) {
                            inclusive = true
                        }
                    }
                }
        }
    )
}
@Composable
fun BottomNavigationBar(navController: NavController) {
    val screens = listOf(
        Screen.Home,
        Screen.Videos,
        Screen.Jobs,
        Screen.Projects,
        Screen.Chat
    )

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val cornerRadius = 18.dp

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color(0xFFE0E0E0), // Light border color
                shape = RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius)
            )
            .clip(RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius))
    ) {
        NavigationBar(containerColor = Color.Transparent) {
            screens.forEach { screen ->
                val isSelected = screen.route == currentRoute
                val icon = if (isSelected) screen.selectedIcon else screen.icon
                NavigationBarItem(
                    icon = {
                        Image(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(icon!!),
                            contentDescription = screen.title
                        )
                    },
                    label = { Text(screen.title!!) },
                    selected = isSelected,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.Gray
                    ),
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(Screen.Home.route) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}

//@Composable
//fun BottomNavigationBar(navController: NavController) {
//    val screens = listOf(
//        Screen.Home,
//        Screen.Videos,
//        Screen.Jobs,
//        Screen.Projects,
//        Screen.Chat
//
//    )
//
//    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
//
//    NavigationBar(containerColor = Color.White, modifier = Modifier.padding(0.dp)) {
//        screens.forEach { screen ->
//            val isSelected = screen.route == currentRoute
//            val icon = if (isSelected) screen.selectedIcon else screen.icon
//            NavigationBarItem(
//                icon = {
//                    Image(
//                        modifier = Modifier.size(24.dp),
//                        painter = painterResource(icon!!),
//                        contentDescription = screen.title,
////                        tint = if (isSelected) Color.Black else Color.Gray
//                    )
//                },
//                label = { Text(screen.title!!) },
//                selected = isSelected,
//                colors = NavigationBarItemDefaults.colors(
//                    selectedIconColor = Color.Black,
//                    unselectedIconColor = Color.Gray,
//                    selectedTextColor = Color.Black,
//                    unselectedTextColor = Color.Gray,
////                    indicatorColor = colorResource(R.color.blue_gradient_color)// Purple background when selected
//                ),
//                onClick = {
//                    navController.navigate(screen.route) {
//                        // Ensure the selected screen isn't added multiple times to the backstack
//                        popUpTo(Screen.Home.route) { inclusive = false }
//                        launchSingleTop = true
//                    }
//                }
//            )
//        }
//    }
//}

@Composable
fun JobItem(job: JobModel, modifier: Modifier) {
    Card(
        modifier
            .fillMaxWidth()
            .padding(10.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box() {
            Column(
                modifier = modifier
                    .padding(8.dp)
            ) {
                job.title?.let {
                    Text(
                        text = it,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 16.dp),
                        textAlign = TextAlign.Start,
                        style = TextStyle(
                            fontSize = 24.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Spacer(Modifier.height(6.dp))

                job.company?.let {
                    Text(
                        text = it,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 16.dp),
                        textAlign = TextAlign.Start,
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }

                job.location?.let {
                    Text(
                        text = it,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 16.dp),
                        textAlign = TextAlign.Start,
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }

                job.type?.let {
                    Text(
                        text = it,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 16.dp),
                        textAlign = TextAlign.Start,
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }

                job.salary?.let {
                    Text(
                        text = it,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 16.dp),
                        textAlign = TextAlign.Start,
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
                Spacer(Modifier.height(6.dp))
            }

            Box(
                modifier
                    .padding(14.dp)
                    .clip(shape = RoundedCornerShape(18.dp))
                    .align(Alignment.BottomEnd)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Blue1, Blue2
                            )
                        )
                    ),
                contentAlignment = Alignment.Center,
            ) {
                val context = LocalContext.current // Get context inside @Composable

                Button(
                    onClick = {
                        job.link?.let { openCustomTab(context, it) }
                    },
                    modifier = Modifier
                        .height(34.dp)
                        .width(100.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Text(
                        text = "Apply",
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = Color.White,
                        )
                    )
                }
            }
        }
    }
}

fun openCustomTab(context: Context, url: String) {
    val intent = CustomTabsIntent.Builder()
        .setShowTitle(true) // Show page title
        .build()

    intent.launchUrl(context, Uri.parse(url))
}
@OptIn(ExperimentalGlideComposeApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostItem(modifier: Modifier = Modifier, post: PostResponse) {
   val viewModel: WebSocketViewModel = viewModel()
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("NextStepPrefs", Context.MODE_PRIVATE)
    val userId = sharedPreferences.getString("USER_ID", "") ?: ""

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // ✅ Profile and User Info
            Row(verticalAlignment = Alignment.CenterVertically) {
                GlideImage(
                    model = post.user?.profilePic ?: R.drawable.profile, // Load user's profile picture
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .border(2.dp, Purple40, CircleShape)
                        .padding(2.dp),
                    contentScale = ContentScale.Crop
                ) {
                    it.load(post.user?.profilePic)
                        .placeholder(R.drawable.profile)
                        .error(R.drawable.profile)
                }

                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text(
                        text = post.user?.name ?: "Unknown User",
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Text(
                        text = formatTimeAgo(post.createdAt),
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            // ✅ Post Content
            Text(
                text = post.content,
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 14.sp
                ),
                modifier = Modifier.padding(start = 4.dp)
            )

            Spacer(Modifier.height(14.dp))

            // ✅ Post Image (if available)
            post.imageUrl?.let {
                GlideImage(
                    model = post.imageUrl,
                    contentDescription = "Profile Picture",
                    modifier =  Modifier
                        .fillMaxWidth().heightIn(max = 500.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop,
                ) {
                    it.load(post.imageUrl,)
                        .placeholder(R.drawable.flowering1)
                        .error(R.drawable.flowering1)
                }

                Spacer(Modifier.height(14.dp))
            }

            // ✅ Post Actions (Like, Comment, Share)
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { /* Handle Like */ }) {
                    Icon(Icons.Outlined.ThumbUp, contentDescription = "Like", modifier = Modifier.size(24.dp))
                }
                Text(text = post.likes.size.toString())

                Spacer(Modifier.width(10.dp))

                IconButton(onClick = { /* Handle Comment */ }) {
                    Icon(Icons.AutoMirrored.Outlined.Chat, contentDescription = "Comment", modifier = Modifier.size(24.dp))
                }
                Text(text = post.comments.size.toString())

                Spacer(Modifier.width(10.dp))

                IconButton(onClick = { /* Handle Share */ }) {
                    Icon(Icons.Outlined.Share, contentDescription = "Share", modifier = Modifier.size(24.dp))
                }
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
fun formatTimeAgo(timestamp: String): String {
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    val time = LocalDateTime.parse(timestamp, formatter)
    val now = LocalDateTime.now()

    val minutes = ChronoUnit.MINUTES.between(time, now)
    val hours = ChronoUnit.HOURS.between(time, now)
    val days = ChronoUnit.DAYS.between(time, now)

    return when {
        minutes < 60 -> "$minutes min ago"
        hours < 24 -> "$hours hours ago"
        days < 7 -> "$days days ago"
        else -> time.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
    }
}
//@Composable
//fun PostItem(modifier: Modifier = Modifier, post: PostResponse) {
//    Card(
//        modifier
//            .fillMaxWidth()
//            .padding(top = 10.dp),
//        colors = CardDefaults.cardColors(containerColor = Color.White),
//    ) {
//        Box() {
//            Column(
//                modifier = modifier
//                    .padding(12.dp)
//            ) {
//                Row {
//                    Image(
//                        Icons.Filled.Person,
//                        contentDescription = "Profile Picture",
//                        modifier = Modifier
//                            .size(38.dp) // Adjust size as needed
//                            .clip(CircleShape)
//                            .border(2.dp, Purple40, CircleShape)
//                            .padding(4.dp),
//                        contentScale = ContentScale.Crop,
//
//                        )
//                    Column {
//                        Text(
//                            modifier = Modifier.padding(start = 12.dp),
//                            text = "Shivam kushwaha",
//                            style = TextStyle(
//                                color = Color.Black,
//                                fontSize = 16.sp,
//                                fontWeight = FontWeight.Medium
//                            )
//                        )
//                        Text(
//                            modifier = Modifier.padding(start = 12.dp, top = 3.dp),
//                            text = "3 days ago",
//                            style = TextStyle(
//                                color = Color.Gray,
//                                fontSize = 14.sp,
//                                fontWeight = FontWeight.Normal
//                            )
//                        )
//                    }
//                }
//                Spacer(Modifier.height(14.dp))
//                Text(
//                    modifier = Modifier.padding(start = 4.dp),
//                    text = "This course is made for people who want to learn DSA from A to Z for free in a well-organized and structured manner. The lecture quality is better than what you get in paid courses, the only thing we don’t provide is doubt support, but trust me our YouTube video comments resolve that as well, we have a wonderful community of 250K+ people who engage in all of the videos.",
//                    style = TextStyle(
//                        color = Color.Black,
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.Normal
//                    )
//                )
//
//                Spacer(Modifier.height(14.dp))
//
//                Image(
//                    painter = painterResource(id = R.drawable.flowering1),
//                    null,
//                    modifier = Modifier.clip(shape = RoundedCornerShape(12.dp))
//                )
//
//                Row(verticalAlignment = Alignment.CenterVertically)
//                {
//                    IconButton(onClick = {},
//                        content = {
//                            Icon(
//                                Icons.Outlined.ThumbUp,
//                                contentDescription = null,
//                                modifier = Modifier
//                                    .size(24.dp)
//                            )
//                        })
//                    Text(text = "4")
//                    Spacer(Modifier.width(10.dp))
//                    IconButton(onClick = {},
//                        content = {
//                            Icon(
//                                Icons.AutoMirrored.Outlined.Chat,
//                                contentDescription = null,
//                                modifier = Modifier
//                                    .size(24.dp)
//                            )
//                        })
//                    Text(text = "4")
//
//                    Spacer(Modifier.width(10.dp))
//                    IconButton(onClick = {},
//                        content = {
//                            Icon(
//                                Icons.Outlined.Share,
//                                contentDescription = null,
//                                modifier = Modifier
//                                    .size(24.dp)
//                            )
//                        })
//                }
//            }
//
//        }
//    }
//
//}

@Composable
fun ProjectItem(
    modifier: Modifier,
    image: Int,
    title: String? = null,
    description: String? = null,
    upvotes: Int? = null
) {

    Box(
        modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp)
    )
    {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(90.dp)
                .fillMaxWidth()
        )
        {
            Image(
                modifier = Modifier
                    .weight(.3f),
                contentScale = ContentScale.Crop,
                painter = painterResource(image), contentDescription = null
            )
            Spacer(Modifier.width(10.dp))
            Box(
                Modifier
                    .height(90.dp)
                    .weight(.5f)
            ) {
                Column {
                    Text(
                        text = "AI Course Generator",
                        modifier = modifier.padding(top = 4.dp),
                        textAlign = TextAlign.Start,
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(Modifier.height(3.dp))

                    Text(
                        text = "Generate courses with the help of AI models.",
                        textAlign = TextAlign.Start,
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.W300
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(bottom = 3.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Icon(
                            Icons.AutoMirrored.Outlined.Chat,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier
                                .size(20.dp)
                        )
                        Text(
                            text = "4",
                            Modifier.padding(start = 4.dp),
                            style = TextStyle(fontSize = 16.sp),

                            )
                    }
                }
            }

            Box(modifier = Modifier.weight(.2f), contentAlignment = Alignment.Center) {
                IconButton(
                    modifier = Modifier.fillMaxSize(), onClick = {},
                    content = {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .border(
                                    0.5.dp,
                                    Color.Gray,
                                    shape = RoundedCornerShape(6.dp)
                                ), contentAlignment = Alignment.Center
                        )
                        {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Filled.KeyboardDoubleArrowUp,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(28.dp)
                                )
                                Text(
                                    text = "54",
                                    style = TextStyle(fontSize = 16.sp),

                                    )
                            }
                        }
                    })
            }
        }

    }

}


@Composable
fun PageIndicator(pagesize: Int, currentpage: Int) {

        Row(
            modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            repeat(pagesize) {
                val color = if (it == currentpage) Color.Black else Color.White
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .height(12.dp)
                        .width(width = if (it == currentpage) 24.dp else 12.dp)
                        .clip(shape = CircleShape)
                        .background(color)
                )
            }
        }
}

@Preview(showSystemUi = true)
@Composable
private fun default() {
    Surface(Modifier.windowInsetsPadding(WindowInsets.statusBars)) {
 //       PageIndicator(3, 2)
//        ProjectItem(Modifier, R.drawable.project_img)
       BottomNavigationBar(navController = rememberNavController())
//        PostItem(Modifier)
//        ClickableLoginTextComponent(rememberNavController())
//        DividerTextComponent(Modifier)
//        ButtonComponent("Sign Up", Modifier)
//        PasswordInputBox("Fullname", Modifier, painterResource(id = R.drawable.baseline_person_24))
    }
}
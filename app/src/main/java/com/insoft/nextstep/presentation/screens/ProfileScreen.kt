package com.insoft.nextstep.presentation.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.insoft.nextstep.R
import com.insoft.nextstep.presentation.viewmodels.ProfileViewModel


@OptIn(ExperimentalLayoutApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    userId: String?,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val user by viewModel.user.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    println(user)

    val sharedPreferences = context.getSharedPreferences("NextStepPrefs", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("USER_TOKEN", null)
    val id = sharedPreferences.getString("USER_ID", null)
    val username = sharedPreferences.getString("USER_NAME", null)
    val profilePic = sharedPreferences.getString("USER_IMAGE", null)

    LaunchedEffect(Unit) {
        viewModel.getUserProfile(userId.toString())
    }
    LaunchedEffect(user?.user?.githubUsername) {
        user?.user?.githubUsername?.let { username ->
            if (username.isNotBlank()) {
                viewModel.getGitHubProfile(username)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Top Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(Color(0xFFEEF5FF))
                .padding(16.dp)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { navController.popBackStack() }
                    )

                    Spacer(modifier = Modifier.width(18.dp))

                    Text(
                        text = (user?.user?.name) ?: "Loading...",
                        style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.SemiBold),
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                navController.navigate("edit_profile")
                            }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    if (!user?.user?.profilePic.isNullOrBlank()) {

                        GlideImage(
                            model = user?.user!!.profilePic,
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(Color.Gray, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = user?.user?.name?.firstOrNull()
                                    ?.toString() ?: "?",
                                fontSize = 28.sp,
                                color = Color.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            user?.user?.name ?: "",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                        Text(
                            user?.user?.bio ?: "No bio added",
                            fontSize = 14.sp,
                            maxLines = 3,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    user?.user?.tags?.forEach {
                        Box(
                            modifier = Modifier
                                .background(
                                    colorResource(R.color.teal_200),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(it.toString(), fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Tabs
        val tabs = listOf("Work", "Resume", "Collections", "Posts")
        var selectedTabIndex by remember { mutableStateOf(0) }

        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            edgePadding = 8.dp,
            contentColor = Color.Black,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = Color.Black
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        // Content for each tab
        when (selectedTabIndex) {
            0 -> GitHubSection(context,user?.user?.githubUsername ?: "")
            1 -> ResumeViewer(user?.user?.resume ?: "")
            2 -> PlaceholderContent("Collections")
            3 -> PlaceholderContent("Posts")
        }
    }

    if (isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }

    error?.let { error ->
        LaunchedEffect(error) {
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }
}
@Composable
fun GitHubSection(
    context: Context,
    githubUsername: String,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    var isChartLoading by remember { mutableStateOf(true) }
    val profile by viewModel.gitHubProfile.collectAsState()

    LaunchedEffect(githubUsername) {
        if (githubUsername.isNotBlank()) {
            viewModel.getGitHubProfile(githubUsername)
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            "GitHub ($githubUsername)",
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (githubUsername.isNotBlank()) {
            profile?.let { profileData ->
//                profileData.name?.takeIf { it.isNotBlank() }?.let {
//                    Text("Name: $it")
//                }

//                profileData.bio?.takeIf { it.isNotBlank() }?.let {
//                    Text("Bio: $it")
//                }

//                profileData.location?.takeIf { it.isNotBlank() }?.let {
//                    Text("Location: $it")
//                }

//                profileData.blog?.takeIf { it.isNotBlank() }?.let {
//                    Text("Blog: $it")
//                }

                Text("Repos: ${profileData.public_repos}")
                Text("Followers: ${profileData.followers} | Following: ${profileData.following}")

                profileData.html_url?.takeIf { it.isNotBlank() }?.let { url ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "View GitHub Profile",
                        color = Color.Blue,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // GitHub Contributions Chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)),
                    factory = { context ->
                        WebView(context).apply {
                            webViewClient = object : WebViewClient() {
                                override fun onPageFinished(view: WebView?, url: String?) {
                                    isChartLoading = false
                                }
                            }
                            settings.javaScriptEnabled = true
                            loadUrl("https://ghchart.rshah.org/$githubUsername")
                        }
                    }
                )

                if (isChartLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.Black)
                    }
                }
            }
        } else {
            Text("No GitHub username available.")
        }
    }
}


@Composable
fun PlaceholderContent(title: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("Coming soon: $title", fontSize = 16.sp, color = Color.Gray)
    }
}

@Composable
fun ResumeViewer(resumeUrl: String) {
    var isLoaded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        if (resumeUrl.isNotBlank()) {
            // Show loader until WebView finishes loading
            if (!isLoaded) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.Black)
                }
            }

            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(if (isLoaded) 1f else 0f), // Hide WebView until loaded
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        settings.loadWithOverviewMode = true
                        settings.useWideViewPort = true
                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                isLoaded = true
                            }
                        }
                        loadUrl("https://docs.google.com/gview?embedded=true&url=$resumeUrl")
                    }
                }
            )
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text("No resume uploaded.", fontSize = 16.sp, color = Color.Gray)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun prev() {
//    ProfileScreen(NavController(LocalContext.current))
}
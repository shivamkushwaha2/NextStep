package com.insoft.nextstep.presentation.screens

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

import coil3.request.allowHardware
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.insoft.nextstep.R
import com.insoft.nextstep.data.model.PostResponse
import com.insoft.nextstep.presentation.components.BottomNavigationBar
import com.insoft.nextstep.presentation.components.PostItem
import com.insoft.nextstep.presentation.components.formatTimeAgo
import com.insoft.nextstep.presentation.viewmodels.PostViewModel
import com.insoft.nextstep.presentation.viewmodels.WebSocketViewModel
import com.insoft.nextstep.ui.theme.Purple40
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import android.graphics.drawable.BitmapDrawable

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: PostViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val sharedPreferences = navController.context.getSharedPreferences("NextStepPrefs", 0)
    val image = sharedPreferences.getString("USER_IMAGE", "")
    val token = sharedPreferences.getString("USER_TOKEN", "") ?: ""
    val userId = sharedPreferences.getString("USER_ID", "") ?: ""

    val posts by viewModel.posts.collectAsState()
    val error by viewModel.error.collectAsState()

    val webSocketViewModel: WebSocketViewModel = viewModel()
    val likes by webSocketViewModel.likesFlowPost.collectAsState()

//    val comments by webSocketViewModel.commentsFlowPost.collectAsState()
    val commentsMap by webSocketViewModel.commentsMapPost.collectAsState()

    val shares by webSocketViewModel.sharesFlowPost.collectAsState()

    LaunchedEffect(Unit) {
        webSocketViewModel.clearCommentsMap() // Avoid duplicates
        viewModel.fetchAllPosts(token)
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        bottomBar = { BottomNavigationBar(navController) },
        topBar = { TopBar(scrollBehavior, "Feed", image) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_post") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Post")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                error != null -> {
                    Text("Error: $error", color = Color.Red, modifier = Modifier.padding(16.dp))
                }

                posts.isEmpty() -> {
                    Text("No posts available", modifier = Modifier.padding(16.dp))
                }

                else -> {
                    LazyColumn {
                        items(posts.size, key = { posts[it]._id }) { index ->
                            val post = posts[index]
                            val initialComments = post.comments
                            val liveComments = commentsMap[post._id] ?: emptyList()

                            // ✅ Merge without duplicate
                            val postComments = (initialComments + liveComments).distinctBy { it._id }

                            val commentCount = postComments.size

                            var showCommentSheet by remember { mutableStateOf(false) }

                            val shareCount = shares[post._id] ?: post.shares.size

                            val likeInfo = likes[post._id]
                            val isLiked = likeInfo?.isLiked ?: post.likes.contains(userId)
                            val likeCount = likeInfo?.count ?: post.likes.size

                            Card(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    // --- User Info ---
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        GlideImage(
                                            model = post.user?.profilePic ?: R.drawable.profile,
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
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 16.sp,
                                                color = Color.Black
                                            )
                                            Text(
                                                text = formatTimeAgo(post.createdAt),
                                                fontSize = 14.sp,
                                                color = Color.Gray
                                            )
                                        }
                                    }

                                    Spacer(Modifier.height(14.dp))

                                    // --- Post Content ---
                                    Text(
                                        text = post.content,
                                        fontSize = 14.sp,
                                        color = Color.Black,
                                        modifier = Modifier.padding(start = 4.dp)
                                    )

                                    Spacer(Modifier.height(14.dp))

                                    // --- Post Image ---
                                    post.imageUrl?.let {
                                        GlideImage(
                                            model = post.imageUrl,
                                            contentDescription = "Post Image",
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .heightIn(max = 500.dp)
                                                .clip(RoundedCornerShape(12.dp)),
                                            contentScale = ContentScale.Crop,
                                        ) {
                                            it.load(post.imageUrl)
                                                .placeholder(R.drawable.flowering1)
                                                .error(R.drawable.flowering1)
                                        }
                                        Spacer(Modifier.height(14.dp))
                                    }

                                    // --- Post Actions ---
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(onClick = {
                                            webSocketViewModel.sendLike_Post(
                                                postId = post._id,
                                                userId = userId,
                                                isLike = !isLiked // Toggle like
                                            )
                                        }) {
                                            Icon(
                                                imageVector = if (isLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                                                contentDescription = "Like",
                                                tint = if (isLiked) MaterialTheme.colorScheme.primary else Color.Black,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                        Text(
                                            text = likeCount.toString(),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Black
                                        )

                                        Spacer(Modifier.width(10.dp))

                                        IconButton(onClick = {
                                            showCommentSheet = true
                                        }) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Outlined.Chat,
                                                contentDescription = "Comment",
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                        Text(
                                            text = commentCount.toString(),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Black
                                        )

                                        Spacer(Modifier.width(10.dp))

                                        IconButton(onClick = {
                                            sharePostWithImage(context, post) // Trigger system share
                                            webSocketViewModel.sendShare_Post(postId = post._id, userId)
                                        }) {
                                            Icon(
                                                imageVector = Icons.Outlined.Share,
                                                contentDescription = "Share",
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                        Text(
                                            text = shareCount.toString(),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Black
                                        )
                                    }
                                }
                            }
                            Divider()

                            if (showCommentSheet) {
                                CommentBottomSheet(
                                    videoId = post._id,
                                    comments = postComments,
                                    onCommentPost = { commentText ->
                                        webSocketViewModel.sendComment_Post(post._id, userId, commentText)
                                        showCommentSheet = false
                                    },
                                    onDismiss = { showCommentSheet = false }
                                )
                            }
                        }

                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
fun TopBar(scrollBehavior: TopAppBarScrollBehavior, title: String, image: String?) {
    TopAppBar(scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(
                onClick = {
                    /* Handle navigation icon click */
                }

            ) {
                GlideImage(
                    model = image,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(48.dp) // Adjust size as needed
                        .clip(CircleShape)
                        .border(2.dp, Purple40, CircleShape)
                        .padding(4.dp),
                    contentScale = ContentScale.Crop,
                ) {
                    it.load(image)
                        .placeholder(R.drawable.profile)
                        .error(R.drawable.profile)
                }

            }
        },
        title = {
            Text(
                text = title,
                modifier = Modifier.padding(start = 8.dp),
                style = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
        actions = {
            IconButton(onClick = {
                /* Handle action icon click */
            }) {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = null,
                    Modifier.size(28.dp)
                )
            }
            IconButton(onClick = {
                /* Handle action icon click */
            }) {
                Icon(
                    Icons.Filled.Notifications,
                    contentDescription = null,
                    Modifier.size(28.dp)
                )
            }
        }
    )
}
fun sharePostWithImage(context: Context, post: PostResponse) {
    val url = post.imageUrl ?: return sharePost(context, post)

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(url)
                .allowHardware(false)
                .build()

            val result = (loader.execute(request) as? SuccessResult)?.drawable
            val bitmap = (result as? BitmapDrawable)?.bitmap

            bitmap?.let {
                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    saveBitmapToCache(context, it)
                )

                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "image/*"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }

                withContext(Dispatchers.Main) {
                    context.startActivity(Intent.createChooser(shareIntent, "Share via"))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            sharePost(context, post)
        }
    }
}
fun sharePost(context: Context, post:  PostResponse) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Check this out!")
        putExtra(Intent.EXTRA_TEXT, post.content + "\n\nDownload NextStep App 📲")
    }

    context.startActivity(Intent.createChooser(intent, "Share via"))
}
fun saveBitmapToCache(context: Context, bitmap: Bitmap): File {
    val cachePath = File(context.cacheDir, "images")
    cachePath.mkdirs()
    val file = File(cachePath, "shared_image.png")
    FileOutputStream(file).use {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
    }
    return file
}
@Preview(showBackground = true)
@Composable
private fun preview() {
//    HomeScreen(rememberNavController(), Modifier)
}
package com.insoft.nextstep.presentation.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddToPhotos
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUpOffAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.insoft.nextstep.R
import com.insoft.nextstep.presentation.viewmodels.WebSocketViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.insoft.nextstep.data.model.LikeInfo
import com.insoft.nextstep.data.model.VideoResponse
import com.insoft.nextstep.presentation.components.BottomNavigationBar
import com.insoft.nextstep.presentation.viewmodels.VideoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoScreen(
    navController: NavController,
    modifier: Modifier,
    viewModel: WebSocketViewModel = viewModel(),
    videoViewModel: VideoViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("NextStepPrefs", Context.MODE_PRIVATE)
    val userId = sharedPreferences.getString("USER_ID", "") ?: ""

    val scrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(state = rememberTopAppBarState())

    Scaffold(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.statusBars),
        contentWindowInsets = WindowInsets(0.dp),
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        ScreenContent(
            viewModel = viewModel,
            videoViewModel = videoViewModel,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(color = Color.White),
            userId = userId,
            navController = navController,
        )
    }
}

@Composable
fun ScreenContent(
    viewModel: WebSocketViewModel,
    videoViewModel: VideoViewModel,
    modifier: Modifier = Modifier,
    userId: String,
    navController: NavController
) {
    val likes by viewModel.likesFlow.collectAsState()
    val comments by viewModel.commentsFlow.collectAsState()

    val videos by videoViewModel.videos.collectAsState()
    val context = LocalContext.current

    println("comments $comments")
    // Ensure Pager State is properly initialized
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { videos.size }
    )

    // Preload videos for smooth transitions
    LaunchedEffect(pagerState.currentPage) {
        if (videos.isNotEmpty()) {
            videoViewModel.getExoPlayer(context, videos[pagerState.currentPage].videoUrl)
            if (pagerState.currentPage < videos.lastIndex) {
                videoViewModel.preloadVideo(context, videos[pagerState.currentPage + 1].videoUrl)
            }
        }
    }

    // Wrap with Box for better control
    Box(modifier = modifier.fillMaxSize()) {
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                // Video Player
                VideoPlayer(
                    videoUrl = videos[page].videoUrl,
                    context = context,
                    viewModel = videoViewModel,
                    isVisible = page == pagerState.currentPage
                )

                // Overlay UI (Like, Comment, Share)
                VideoOverlayUI(
                    video = videos[page],
                    userId = userId,
                    viewModel = viewModel,
                    likes = likes,
                    comments = comments,
                    modifier = Modifier.fillMaxSize(),
                    navController = navController
                )
            }
        }
    }
}

@Composable
private fun VideoOverlayUI(
    video: VideoResponse,
    userId: String,
    viewModel: WebSocketViewModel,
    likes: Map<String, LikeInfo>,
    comments: Map<String, Int>,
    modifier: Modifier = Modifier,
    navController: NavController
) {

    val context = LocalContext.current

    val likeInfo = likes[video._id]
    val isLiked = likeInfo?.isLiked ?: video.likes.contains(userId)
    val likeCount = likeInfo?.count ?: video.likes.size

    val shares by viewModel.sharesFlow.collectAsState()
    val shareCount =
        if (shares[video._id] != null) shares[video._id].toString() else video.shares.size.toString()

    val commentCount by rememberUpdatedState(
        comments[video._id]?.toString() ?: video.comments.size.toString()
    )
    var showCommentSheet by remember { mutableStateOf(false) }


    Box(modifier = modifier)
    {
        IconButton(
            onClick = {
                navController.navigate("uploadVideo")
            },
            modifier = Modifier
                .size(72.dp)
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 12.dp)
        ) {
            Column {
                Icon(
                    imageVector = Icons.Default.AddToPhotos,
                    contentDescription = "Create",
                    tint = Color.White,
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "Create",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }

        }

        Column(Modifier.align(Alignment.BottomCenter)) {
            // Right side action buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp),
                horizontalAlignment = Alignment.End
            )
            {
                IconButton(
                    onClick = {
                        viewModel.sendLike(videoId = video._id, userId, isLike = !isLiked)
                    },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        painter = if (isLiked) painterResource(R.drawable.heartpink) else painterResource(R.drawable.heart),
                        contentDescription = "Like",
                        tint =  if (isLiked) Color.Unspecified else Color.White, // Change color based on state
                        modifier = Modifier.size(32.dp)
                    )
                }
                Text(
                    text = likeCount.toString(),
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(end = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                IconButton(
                    onClick = { showCommentSheet = true }, // ✅ Open BottomSheet
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.comment),
                        contentDescription = "Comment",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Text(
                    text = commentCount,
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(end = 16.dp)

                )

                Spacer(modifier = Modifier.height(16.dp))

                IconButton(
                    onClick = {
                        val shareText = "${video.description}\n\nWatch now: ${video.videoUrl}"

                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, shareText)
                            type = "text/plain"
                        }

                        val shareIntent = Intent.createChooser(sendIntent, "Share Video")
                        context.startActivity(shareIntent)

                        viewModel.sendShare(videoId = video._id, userId)
                     },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                       painter = painterResource(R.drawable.share),
                        contentDescription = "Share",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Text(
                    text = shareCount.toString(),
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(end = 16.dp)
                )

            }
            if (showCommentSheet) {
                CommentBottomSheet(
                    videoId = video._id,
                    comments = video.comments, // ✅ Pass comments
                    onCommentPost = { commentText ->
                        viewModel.sendComment(video._id, userId, commentText)
                        showCommentSheet = false
                    },
                    onDismiss = { showCommentSheet = false }
                )
            }

            // Bottom info and description
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 8.dp, bottom = 8.dp),
                horizontalAlignment = Alignment.Start
            )
            {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.profile),
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = video.user.name,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .background(Color.Blue, shape = RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { /* Handle follow */ }
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Follow",
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
                Text(
                    text = video.description,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    videoUrl: String,
    context: Context,
    viewModel: VideoViewModel,
    isVisible: Boolean
) {
    val exoPlayer = remember(videoUrl) { viewModel.getExoPlayer(context, videoUrl) }

    LaunchedEffect(isVisible) {
        exoPlayer.playWhenReady = isVisible
        if (isVisible) {
            exoPlayer.play()
        } else {
            exoPlayer.pause()
        }
    }

    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.playWhenReady = false
            exoPlayer.pause()
        }
    }

    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
                useController = false
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}


@Preview(showBackground = true)
@Composable
private fun ScreenContentPreview() {
//    VideoScreen(
//        NavController(LocalContext.current),
//        Modifier,
//        "videoId",
//    )
}


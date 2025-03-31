package com.insoft.nextstep.presentation.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.AddCircleOutline
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
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
import com.insoft.nextstep.data.model.VideoModel
import com.insoft.nextstep.presentation.components.BottomNavigationBar
import com.insoft.nextstep.presentation.viewmodels.VideoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoScreen(
    navController: NavController,
    modifier: Modifier,
    userId: String,
    viewModel: WebSocketViewModel = viewModel(),
    videoViewModel: VideoViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(state = rememberTopAppBarState())

    Scaffold(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.statusBars),
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        ScreenContent(
            viewModel = viewModel,
            videoViewModel = videoViewModel,
            modifier = Modifier.padding(paddingValues),
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
    val videos by videoViewModel.videos.collectAsState()
    val context = LocalContext.current

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
                    modifier = Modifier.fillMaxSize(),
                    navController = navController
                )
            }
        }
    }
}

@Composable
private fun VideoOverlayUI(
    video: VideoModel,
    userId: String,
    viewModel: WebSocketViewModel,
    likes: Map<String, Int>,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var isLiked by remember { mutableStateOf(video.likes.contains(userId)) }
    val likeCount = if(likes[video._id] !=null ) likes[video._id].toString() else video.likes.size.toString()

    Box(modifier = modifier)
    {
        // Right side action buttons
        Column(
            modifier = Modifier
                .padding(end = 16.dp)
                .align(Alignment.BottomEnd),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            IconButton(
                onClick = {
                    isLiked = !isLiked  // Toggle like state
                    viewModel.sendLike(videoId = video._id, userId, isLiked)
                },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ThumbUpOffAlt,
                    contentDescription = "Like",
                    tint = if (isLiked) Color.Blue else Color.White, // Change color based on state
                    modifier = Modifier.size(32.dp)
                )
            }
//            val like = if(likes[video._id] !=null ) likes[video._id].toString() else video.likes.size.toString()
            Text(
                text = likeCount,
                color = Color.Black,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            IconButton(
                onClick = { viewModel.sendComment(video._id, userId, "Nice Video") },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ChatBubbleOutline,
                    contentDescription = "Comment",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
            Text(
                text = "100",
                color = Color.White,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            IconButton(
                onClick = { /* Handle share */ },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
            Text(
                text = "100",
                color = Color.White,
                style = MaterialTheme.typography.bodySmall
            )


            IconButton(
                onClick = {
                    navController.navigate("uploadVideo")
                },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircleOutline,
                    contentDescription = "Create",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
            Text(
                text = "Create",
                color = Color.White,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(modifier = Modifier.height(16.dp))


        // Bottom info and description
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
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
                    text = "username",
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
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            Text(
                text = "description description description description description description description description",
                color = Color.White,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
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
    VideoScreen(
        NavController(LocalContext.current),
        Modifier,
        "videoId" ,
    )
}


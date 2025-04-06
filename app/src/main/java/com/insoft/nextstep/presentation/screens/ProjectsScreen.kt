package com.insoft.nextstep.presentation.screens

import Project
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.KeyboardDoubleArrowUp
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.insoft.nextstep.R
import com.insoft.nextstep.presentation.components.BottomNavigationBar
import com.insoft.nextstep.presentation.components.ProjectItem
import com.insoft.nextstep.presentation.viewmodels.ProjectViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsScreen(
    viewModel: ProjectViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val projects = viewModel.projectList
    val isLoading = viewModel.isLoading
    val context = LocalContext.current
    Scaffold(
        topBar = { TopAppBar(title = { Text("Projects") }) },
        bottomBar = { BottomNavigationBar(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("create_project") },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Project")
            }
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.padding(paddingValues).align(Alignment.Center))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(projects.size) { index ->
                    ProjectItem(project = projects[index], onCommentClick = {}, onUpvoteClick = {}, context)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProjectItem(
    project: Project,
    onCommentClick: (Project) -> Unit,
    onUpvoteClick: (Project) -> Unit,
    context: Context
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            // Project image
            GlideImage(
                model = project.image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.FillWidth
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = project.title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            // Description
            Text(
                text = project.description,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )

            // Links (GitHub, Live)
            Row(
                modifier = Modifier
                    .padding(start = 12.dp, top = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
                , horizontalArrangement = Arrangement.Start
            ) {
                if (!project.githubLink.isNullOrEmpty()) {
                    Icon(
                        painter = painterResource(R.drawable.github),
                        contentDescription = "GitHub",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(project.githubLink))
                                ContextCompat.startActivity(context, intent, null)
                            }
                    )
                    Spacer(modifier = Modifier.width(18.dp))
                }

                if (!project.liveLink.isNullOrEmpty()) {
                    Icon(
                        painter = painterResource(R.drawable.link),
                        contentDescription = "Live Link",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(project.liveLink))
                                ContextCompat.startActivity(context, intent, null)
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Footer: Profile, Upvotes, Comments
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile pic
                GlideImage(
                    model = project.postedBy.profilePic,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text("By ${project.postedBy.username}", fontSize = 12.sp)

                Spacer(modifier = Modifier.weight(1f))

                // Upvote
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { onUpvoteClick(project) }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardDoubleArrowUp,
                        contentDescription = "Upvote",
                        tint = Color.DarkGray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${project.upvotes.size}")
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Comment
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { onCommentClick(project) }
                        .padding(horizontal = 4.dp, vertical = 4.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.comment),
                        contentDescription = "Comment",
                        tint = Color.DarkGray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${project.comments.size}")
                }
            }
        }
    }
}

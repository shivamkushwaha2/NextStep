package com.insoft.nextstep.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.insoft.nextstep.R
import com.insoft.nextstep.presentation.components.BottomNavigationBar
import com.insoft.nextstep.presentation.components.PostItem
import com.insoft.nextstep.ui.theme.Purple40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, modifier: Modifier = Modifier) {
//    StatusBarColor(colorResource(R.color.blue_gradient_color))
    val scrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(state = rememberTopAppBarState())
    Scaffold(modifier
        .nestedScroll(scrollBehavior.nestedScrollConnection),

        bottomBar = {
            BottomNavigationBar(navController)
        },
        topBar = {
            TopBar(scrollBehavior, "Feed")
        }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            content = {
                items(10) {
                    Box(
                        Modifier
                            .height(0.5.dp)
                            .background(Color.Gray)
                            .fillMaxWidth()
                    )
                    PostItem()
                }
            }
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopBar(scrollBehavior: TopAppBarScrollBehavior, title: String) {
    TopAppBar(scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(
                onClick = {
                    /* Handle navigation icon click */
                }

            ) {
                Image(
                    painter = painterResource(R.drawable.profile),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(48.dp) // Adjust size as needed
                        .clip(CircleShape)
                        .border(2.dp, Purple40, CircleShape)
                        .padding(4.dp),
                    contentScale = ContentScale.Crop,

                    )
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

@Preview(showBackground = true)
@Composable
private fun preview() {
    HomeScreen(rememberNavController(), Modifier)
}
package com.insoft.nextstep.presentation.screens


import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.insoft.nextstep.presentation.components.BottomNavigationBar
import com.insoft.nextstep.presentation.components.JobItem
import com.insoft.nextstep.presentation.viewmodels.JobViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobScreen(navController: NavHostController, modifier: Modifier = Modifier,  viewModel: JobViewModel = hiltViewModel()) {
//    StatusBarColor(colorResource(R.color.blue_gradient_color))
    val jobs by viewModel.jobs.collectAsState(emptyList())
    val sharedPreferences = navController.context.getSharedPreferences("NextStepPrefs", 0)
    val image = sharedPreferences.getString("USER_IMAGE", "")
    val scrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(state = rememberTopAppBarState())
    Scaffold(modifier
        .windowInsetsPadding(WindowInsets.statusBars)
        .nestedScroll(scrollBehavior.nestedScrollConnection),
        bottomBar = {
            BottomNavigationBar(navController)
        },
        topBar = {
            TopBar(scrollBehavior, "Jobs", image)
        }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            content = {
                items(jobs.size) { it: Int ->
                    JobItem(jobs[it],Modifier)
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun preview() {
    JobScreen(rememberNavController(), Modifier)
}
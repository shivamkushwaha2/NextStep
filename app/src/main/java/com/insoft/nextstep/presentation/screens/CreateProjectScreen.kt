package com.insoft.nextstep.presentation.screens

import Postedby
import Project
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.insoft.nextstep.presentation.viewmodels.ProjectViewModel
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import coil3.compose.rememberAsyncImagePainter
import com.insoft.nextstep.data.model.PostedBy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProjectScreen(
    navController: NavHostController,
    viewModel: ProjectViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }
    var techStack by remember { mutableStateOf("") }
    var githubLink by remember { mutableStateOf("") }
    var liveLink by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isimagePicked by remember { mutableStateOf(false) }

    val sharedPreferences = context.getSharedPreferences("NextStepPrefs", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("USER_TOKEN", null)
    val userId = sharedPreferences.getString("USER_ID", null)
    val username = sharedPreferences.getString("USER_NAME", null)
    val profilePic = sharedPreferences.getString("USER_IMAGE", null)

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
        isimagePicked = uri != null
        viewModel.selectedImageUri = uri
    }
    val loading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Create Project") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp), // Bigger box
                maxLines = 10
            )

            OutlinedTextField(
                value = tags,
                onValueChange = { tags = it },
                label = { Text("Tags (comma-separated)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = techStack,
                onValueChange = { techStack = it },
                label = { Text("Tech Stack (comma-separated)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = githubLink,
                onValueChange = { githubLink = it },
                label = { Text("GitHub Link") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = liveLink,
                onValueChange = { liveLink = it },
                label = { Text("Live Link (if any)") },
                modifier = Modifier.fillMaxWidth()
            )
            if (isimagePicked) {
                Text(
                    text = imageUri.toString(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            // Image Picker Button
            Button(
                onClick = { imageLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = if (imageUri != null) "Image Selected" else "Upload Project Image")
            }


            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val project = Project(
                        id = "",
                        title = title,
                        description = description,
                        tags = tags.split(",").map { it.trim() },
                        image = imageUri?.toString() ?: "",
                        postedBy = Postedby(
                            userId = userId.toString(), // Replace with SharedPrefs
                            username = username.toString(),
                            profilePic = profilePic.toString()
                        ),
                        upvotes = emptyList(),
                        comments = emptyList(),
                        createdAt = "",
                        githubLink = githubLink,
                        liveLink = liveLink,
                        techStack = techStack.split(",").map { it.trim() }
                    )
                    val file = viewModel.getFileFromUri(context)

                    viewModel.createProject(token,project, file) {
                        Toast.makeText(context, "Project created!", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading,
            ) {
                Text(if (loading) "Creating..." else "Submit")
            }
            errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

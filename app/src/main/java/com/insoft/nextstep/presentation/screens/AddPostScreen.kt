package com.insoft.nextstep.presentation.screens

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.insoft.nextstep.presentation.viewmodels.PostViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPostScreen(navController: NavController, viewModel: PostViewModel = hiltViewModel()) {
    var postContent by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val postResponse by viewModel.postResponse.collectAsState()

    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImageUri = uri
    }
    LaunchedEffect(postResponse) {
        postResponse?.let { response ->
            if (response.isSuccessful) {
                Toast.makeText(context, "Post uploaded!", Toast.LENGTH_SHORT).show()
                navController.popBackStack() // Navigate back
            } else {
                Toast.makeText(context, "Failed to upload post", Toast.LENGTH_SHORT).show()
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Post") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
        ) {
            // ✅ TextField takes most of the height
            Box(
                modifier = Modifier
                    .weight(1f) // Makes it expand to fill available space
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(10.dp))
            ) {
                OutlinedTextField(
                    value = postContent,
                    onValueChange = { postContent = it },
                    label = { Text("What's on your mind?") },
                    modifier = Modifier.fillMaxSize(),
                    maxLines = Int.MAX_VALUE, // Allows full expansion
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.Black
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Show Selected Image Preview
            selectedImageUri?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            // Image Picker Button
            Button(
                onClick = { imagePickerLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Image, contentDescription = "Select Image")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Pick an Image")
            }

            // ✅ Post Button at the bottom
            Button(
                onClick = {
                    if (postContent.isNotEmpty()) {
                        val sharedPreferences = context.getSharedPreferences("NextStepPrefs", Context.MODE_PRIVATE)
                        val token = sharedPreferences.getString("USER_TOKEN", null)

                        val contentRequestBody = postContent.toRequestBody("text/plain".toMediaTypeOrNull())

                        val imagePart = selectedImageUri?.let { uri ->
                            val inputStream = context.contentResolver.openInputStream(uri)
                            val byteArray = inputStream?.readBytes()
                            inputStream?.close()

                            byteArray?.let {
                                val requestBody = it.toRequestBody("image/*".toMediaTypeOrNull())
                                MultipartBody.Part.createFormData("image", "upload.jpg", requestBody)
                            }
                        }

                        viewModel.createPost(token.orEmpty(), contentRequestBody, imagePart)
                    } else {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
            ) {
                Text("Post", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}


@Preview
@Composable
private fun pre() {
    AddPostScreen(NavHostController(LocalContext.current))
}

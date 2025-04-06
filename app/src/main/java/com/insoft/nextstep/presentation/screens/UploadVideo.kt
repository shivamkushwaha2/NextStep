package com.insoft.nextstep.presentation.screens

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddToPhotos
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.insoft.nextstep.R
import com.insoft.nextstep.presentation.viewmodels.UploadVideoViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadVideo(
    viewModel: UploadVideoViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val context = LocalContext.current
    var selectedVideoUri by remember { mutableStateOf<Uri?>(null) }
    var isVideoPicked by remember { mutableStateOf(false) }
    var showProgress by remember { mutableStateOf(false) }
    var description by remember { mutableStateOf("") }

    val uploadSuccess by viewModel.uploadSuccess.collectAsState()

    val sharedPreferences = context.getSharedPreferences("NextStepPrefs", Context.MODE_PRIVATE)
    val savedUserId = sharedPreferences.getString("USER_ID", "") ?: ""

    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedVideoUri = uri
        isVideoPicked = uri != null
    }

    LaunchedEffect(uploadSuccess) {
        uploadSuccess?.let { success ->
            showProgress = false
            val msg = if (success) "Upload Successful!" else "Upload Failed!"
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.resetUploadState()
            if (success) navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Upload Video") },
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
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(10.dp))
            ) {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Write a description...") },
                    modifier = Modifier.fillMaxSize(),
                    maxLines = Int.MAX_VALUE,
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

            if (isVideoPicked) {
                Text(
                    text = selectedVideoUri.toString(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = { videoPickerLauncher.launch("video/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.VideoLibrary, contentDescription = "Pick Video")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Pick a Video")
            }

            Button(
                onClick = {
                    showProgress = true
                    val videoFile = selectedVideoUri?.let { uriToFile(it, context) }
                    if (videoFile != null) {
                        viewModel.uploadVideo(videoFile, savedUserId, description)
                    } else {
                        showProgress = false
                        Toast.makeText(context, "Please select a video", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
            ) {
                Text("Upload Video", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }

        if (showProgress) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(20.dp))
                    Text("Uploading video...", color = Color.White)
                }
            }
        }
    }
}



//@Composable
//fun UploadVideo(
//    viewModel: UploadVideoViewModel = hiltViewModel(),
//    navController: NavHostController
//) {
//    val context = LocalContext.current
//    var selectedVideoUri by remember { mutableStateOf<Uri?>(null) }
//    var selectedfile by remember { mutableStateOf("") }
//    var isvisible by remember { mutableStateOf(true) }
//
//    val uploadSuccess by viewModel.uploadSuccess.collectAsState()
//    var showProgress by remember { mutableStateOf(false) } // Single state for progress
//
//    var description by remember { mutableStateOf("") }
//
//    val sharedPreferences = context.getSharedPreferences("NextStepPrefs", Context.MODE_PRIVATE)
//    val savedUserId = sharedPreferences.getString("USER_ID", "") ?: ""
//    val savedUserName = sharedPreferences.getString("USER_NAME", "") ?: ""
//
//    val videoPickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        selectedVideoUri = uri
//        selectedfile = " $selectedVideoUri"
//        isvisible = false
//
//    }
//    Box(Modifier.fillMaxSize()) {
//        Column {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(500.dp)
//            ) {
//                OutlinedTextField(
//                    value = description,
//                    onValueChange = { description = it },
//                    label = { Text("Description..") },
//                    colors = TextFieldDefaults.colors(
//                        unfocusedContainerColor = Color.Transparent,
//                        focusedContainerColor = Color.Transparent,
//                        unfocusedIndicatorColor = Color.Transparent,
//                        focusedIndicatorColor = Color.Transparent,
//                        focusedTextColor = Color.Black
//                    )
//                )
//            }
//
//
//
//            IconButton(
//                onClick = {
//                    videoPickerLauncher.launch("video/*")
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//            ) {
//                Row(
//                    Modifier
//                        .fillMaxWidth()
//                        .padding(end = 16.dp),
//                    horizontalArrangement = Arrangement.End,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text = selectedfile,
//                        color = Color.Black,
//                        style = TextStyle(
//                            fontSize = 18.sp,
//                        ),
//                        modifier = Modifier
//                            .weight(1f)
//                            .padding(start = 16.dp, end = 6.dp),
//                        maxLines = 1,
//                        overflow = TextOverflow.Ellipsis
//                    )
//                    if (isvisible) {
//                        Text(
//                            text = "Add Video",
//                            color = Color.Black,
//                            style = TextStyle(
//                                fontSize = 18.sp,
//                            ),
//                        )
//                    }
//                    Icon(
//                        imageVector = Icons.Default.AddToPhotos,
//                        contentDescription = "Like",
//                        modifier = Modifier.size(42.dp)
//                    )
//
//                }
//            }
//
//            Button(
//                onClick = {
//                    showProgress = true
//
//                    val videoFile = selectedVideoUri?.let {
//                        uriToFile(it, context)
//                    }
//                    if (videoFile != null) {
//                        viewModel.uploadVideo(videoFile, savedUserId, description)
//                    } else {
//                        showProgress = false
//                    }
//
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//                    .heightIn(40.dp),
//                contentPadding = PaddingValues(),
//                colors = ButtonDefaults.buttonColors(Color.Transparent),
//            ) {
//                Box(
//                    Modifier
//                        .fillMaxWidth()
//                        .heightIn(40.dp)
//                        .background(
//                            Brush.linearGradient(
//                                listOf(
//                                    colorResource(R.color.blue1),
//                                    colorResource(R.color.blue2)
//                                )
//                            )
//                        ),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = "Upload Video",
//                        style = TextStyle(
//                            fontSize = 18.sp,
//                            color = Color.White,
//                        )
//                    )
//                }
//            }
//        }
//
//    }
//    uploadSuccess?.let { success ->
//        showProgress = false
//        if (success) {
//            Toast.makeText(LocalContext.current, "Upload Successful!", Toast.LENGTH_SHORT).show()
//            viewModel.resetUploadState()
//            navController.popBackStack()
//        } else {
//
//            Toast.makeText(LocalContext.current, "Upload Failed!", Toast.LENGTH_SHORT).show()
//            viewModel.resetUploadState()
//        }
//    }
//    // Show progress only when needed
//    if (showProgress) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.Black.copy(alpha = 0.5f)),
//            contentAlignment = Alignment.Center
//        ) {
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                CircularProgressIndicator()
//                Spacer(modifier = Modifier.height(20.dp))
//                Text("Uploading video...")
//            }
//
//        }
//    }
//
//}
//
fun uriToFile(uri: Uri, context: Context): File? {
    val contentResolver: ContentResolver = context.contentResolver
    val fileName = "temp_video_${System.currentTimeMillis()}.mp4"
    val tempFile = File(context.cacheDir, fileName)

    return try {
        contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(tempFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        tempFile
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}


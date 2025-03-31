package com.insoft.nextstep.presentation.screens

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.insoft.nextstep.presentation.viewmodels.UploadVideoViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Composable
fun UploadVideo(viewModel: UploadVideoViewModel = hiltViewModel(), navController: NavHostController) {
    val context = LocalContext.current
    var selectedVideoUri by remember { mutableStateOf<Uri?>(null) }
    val uploadSuccess by viewModel.uploadSuccess.collectAsState()

    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedVideoUri = uri
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { videoPickerLauncher.launch("video/*") }) {
            Text("Select Video")
        }

        selectedVideoUri?.let { uri ->
            Text("Selected: ${uri.lastPathSegment}")
            Button(onClick = {
                val videoFile = uriToFile(uri, context)
                if (videoFile != null) {
                    viewModel.uploadVideo(videoFile, "65a2bcdef1e2d34a2f89c789")
                }
            }) {
                Text("Upload Video")
            }
        }

        uploadSuccess?.let { success ->
            if (success) Text("Upload Successful!", color = Color.Green)
            else Text("Upload Failed!", color = Color.Red)
        }
    }
}
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

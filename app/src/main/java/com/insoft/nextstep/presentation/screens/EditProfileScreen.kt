package com.insoft.nextstep.presentation.screens
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.insoft.nextstep.R
import com.insoft.nextstep.data.model.userProfileX
import com.insoft.nextstep.presentation.viewmodels.ProfileViewModel

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val sharedPreferences = context.getSharedPreferences("NextStepPrefs", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("USER_TOKEN", null)
    val userId = sharedPreferences.getString("USER_ID", null)
    val username = sharedPreferences.getString("USER_NAME", null)
    val profilePic = sharedPreferences.getString("USER_IMAGE", null)

    var first = username?.split(" ")?.getOrNull(0) ?: ""
    var last = username?.split(" ")?.getOrNull(1) ?: ""

    var firstName by remember { mutableStateOf(first) }
    var lastName by remember { mutableStateOf(last) }

    var bio by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }
    var githubUsername by remember { mutableStateOf("") }

    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    var resumeUri by remember { mutableStateOf<Uri?>(null) }

    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        profileImageUri = uri
    }

    val resumePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        resumeUri = uri
    }

    // Observe ViewModel StateFlows
    val isLoading by profileViewModel.isLoading.collectAsState()
    val error by profileViewModel.error.collectAsState()
    val user by profileViewModel.user.collectAsState()

    // Show toast on error
    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    // Navigate back when update is successful
    LaunchedEffect(user) {
        user?.let {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text("Basic Profile", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(12.dp))

                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Box {
                        if (profileImageUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(profileImageUri),
                                contentDescription = "Selected Profile Image",
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .background(Color.LightGray),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            GlideImage(
                                model = profilePic,
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(Color.LightGray),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Avatar",
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .offset(8.dp, 8.dp)
                                .size(20.dp)
                                .background(Color.White, CircleShape)
                                .padding(3.dp)
                                .clickable {
                                    imagePicker.launch("image/*")
                                },
                            tint = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = firstName,
                        onValueChange = {
                            firstName = it
                            first = it
                        },
                        label = { Text("First name *") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = lastName,
                        onValueChange = {
                            lastName = it
                            last = it
                        },
                        label = { Text("Last name *") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("Bio") },
                    maxLines = 4,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = tags,
                    onValueChange = { tags = it },
                    label = { Text("Tags (comma-separated)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = githubUsername,
                    onValueChange = { githubUsername = it },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.github),
                            contentDescription = null
                        )
                    },
                    label = { Text("GitHub Username") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedButton(
                    onClick = { resumePicker.launch("application/pdf") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.UploadFile, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(resumeUri?.lastPathSegment ?: "Select Resume PDF")
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (userId != null) {
                            profileViewModel.updateUserProfile(
                                token = token ?: "",
                                context = context,
                                userId = userId,
                                firstName = first,
                                lastName = last,
                                bio = bio,
                                tags = tags,
                                githubUsername = githubUsername,
                                profileImageUri = profileImageUri,
                                resumeUri = resumeUri
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text("Update Profile")
                }
            }
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
//@Composable
//fun EditProfileScreen(
//    navController: NavController,
//    profileViewModel: ProfileViewModel = hiltViewModel()
//) {
//    val context = LocalContext.current
//
//    val sharedPreferences = context.getSharedPreferences("NextStepPrefs", Context.MODE_PRIVATE)
//    val token = sharedPreferences.getString("USER_TOKEN", null)
//    val userId = sharedPreferences.getString("USER_ID", null)
//    val username = sharedPreferences.getString("USER_NAME", null)
//    val profilePic = sharedPreferences.getString("USER_IMAGE", null)
//
//    var first = username?.split(" ")?.getOrNull(0) ?: ""
//    var last = username?.split(" ")?.getOrNull(1) ?: ""
//
//    var firstName by remember { mutableStateOf(first) }
//    var lastName by remember { mutableStateOf(last) }
//
//    var bio by remember { mutableStateOf("") }
//    var tags by remember { mutableStateOf("") }
//    var githubUsername by remember { mutableStateOf("") }
//
//    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
//    var resumeUri by remember { mutableStateOf<Uri?>(null) }
//
//    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//        profileImageUri = uri
//    }
//
//    val resumePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//        resumeUri = uri
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Edit Profile", fontWeight = FontWeight.Bold) },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//                    }
//                }
//            )
//        }
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .padding(innerPadding)
//                .padding(16.dp)
//                .verticalScroll(rememberScrollState())
//        ) {
//            Text("Basic Profile", fontWeight = FontWeight.Bold, fontSize = 18.sp)
//            Spacer(modifier = Modifier.height(12.dp))
//
//            Box(
//                modifier = Modifier.fillMaxWidth(),
//                contentAlignment = Alignment.Center
//            ) {
//                Box {
//                    if (profileImageUri != null) {
//                        Image(
//                            painter = rememberAsyncImagePainter(profileImageUri),
//                            contentDescription = "Selected Profile Image",
//                            modifier = Modifier
//                                .size(72.dp)
//                                .clip(CircleShape)
//                                .background(Color.LightGray),
//                            contentScale = ContentScale.Crop
//                        )
//                    } else {
//                        GlideImage(
//                            model = profilePic,
//                            contentDescription = "Profile Picture",
//                            modifier = Modifier
//                                .size(64.dp)
//                                .clip(CircleShape)
//                                .background(Color.LightGray),
//                            contentScale = ContentScale.Crop
//                        )
//                    }
//
//                    Icon(
//                        imageVector = Icons.Default.Edit,
//                        contentDescription = "Edit Avatar",
//                        modifier = Modifier
//                            .align(Alignment.BottomEnd)
//                            .offset(8.dp, 8.dp)
//                            .size(20.dp)
//                            .background(Color.White, CircleShape)
//                            .padding(3.dp)
//                            .clickable {
//                                imagePicker.launch("image/*")
//                            },
//                        tint = Color.Black
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
//                OutlinedTextField(
//                    value = firstName,
//                    onValueChange = { firstName = it
//                                    first = it},
//                    label = { Text("First name *") },
//                    modifier = Modifier.weight(1f)
//                )
//                OutlinedTextField(
//                    value = lastName,
//                    onValueChange = { lastName = it
//                                    last = it},
//                    label = { Text("Last name *") },
//                    modifier = Modifier.weight(1f)
//                )
//            }
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            OutlinedTextField(
//                value = bio,
//                onValueChange = { bio = it },
//                label = { Text("Bio") },
//                maxLines = 4,
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            OutlinedTextField(
//                value = tags,
//                onValueChange = { tags = it },
//                label = { Text("Tags (comma-separated)") },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            OutlinedTextField(
//                value = githubUsername,
//                onValueChange = { githubUsername = it },
//                leadingIcon = {
//                    Icon(
//                        painter = painterResource(id = R.drawable.github),
//                        contentDescription = null
//                    )
//                },
//                label = { Text("GitHub Username") },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            OutlinedButton(
//                onClick = { resumePicker.launch("application/pdf") },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Icon(Icons.Default.UploadFile, contentDescription = null)
//                Spacer(modifier = Modifier.width(8.dp))
//                Text(resumeUri?.lastPathSegment ?: "Select Resume PDF")
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            Button(
//                onClick = {
//                    if (userId != null) {
//                        profileViewModel.updateUserProfile(
//                            token = token ?: "",
//                            context = context,
//                            userId = userId,
//                            firstName = first,
//                            lastName = last,
//                            bio = bio,
//                            tags = tags,
//                            githubUsername = githubUsername,
//                            profileImageUri = profileImageUri,
//                            resumeUri = resumeUri
//                        )
//                    }
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(52.dp)
//            ) {
//                Text("Update Profile")
//            }
//        }
//    }
//}
//


@Preview
@Composable
private fun prev() {
    EditProfileScreen(navController = NavController(LocalContext.current))
}
package com.insoft.nextstep.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.insoft.nextstep.R
import com.insoft.nextstep.data.model.Comment

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun CommentBottomSheet(
    videoId: String,
    comments: List<Comment>,
    onCommentPost: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var commentText by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .heightIn(min = 200.dp, max = 400.dp)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                "Comments",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(comments.size) { index ->
                    val comment = comments[index]
                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        GlideImage(
                            model = comment.user.profilePic ?: R.drawable.profile,
                            contentDescription = "User profile pic",
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color.Gray, CircleShape),
                            contentScale = ContentScale.Crop
                        ) {
                            it.load(comment.user.profilePic)
                                .placeholder(R.drawable.profile)
                                .error(R.drawable.profile)
                        }

                        Column(modifier = Modifier.padding(start = 12.dp)) {
                            Text(
                                text = comment.user.name,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.Black
                            )
                            Text(
                                text = comment.text,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.DarkGray
                            )
                        }
                    }
                    HorizontalDivider()
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                OutlinedTextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    label = {
                        Text(
                            text = "Comment",
                            color = Color.DarkGray,
                            fontSize = 14.sp
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if (commentText.isNotBlank()) {
                                    onCommentPost(commentText)
                                    commentText = ""
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Post Comment"
                            )
                        }
                    },
                    textStyle = TextStyle(fontSize = 14.sp),
                    keyboardOptions = KeyboardOptions.Default,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0x1DF4F6F6),
                        unfocusedContainerColor = Color(0xFFF6F6F6),
                    )
                )
            }
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CommentBottomSheet(
//    videoId: String,
//    comments: List<Comment>,
//    onCommentPost: (String) -> Unit,
//    onDismiss: () -> Unit
//) {
//    var commentText by remember { mutableStateOf("") }
//
//    ModalBottomSheet(
//        onDismissRequest = { onDismiss() },
//        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
//    ) {
//        Column(
//            modifier = Modifier.heightIn(min = 200.dp, max = 400.dp)
//                .fillMaxWidth()
//                .padding(16.dp)
//        ) {
//            Text(
//                "Comments",
//                style = MaterialTheme.typography.titleLarge,
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
//
//            LazyColumn(modifier = Modifier.weight(1f)) {
//                items(comments.size) { pos ->
//                    Text(
//                        text = comments[pos].text,
//                        modifier = Modifier.padding(8.dp),
//                        style = MaterialTheme.typography.bodyMedium,
//                        color = Color.Black
//                    )
//                    HorizontalDivider()
//                }
//            }
//
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 6.dp),
//                contentAlignment = Alignment.BottomCenter
//            ) {
//                OutlinedTextField(
//                    value = commentText,
//                    label = {
//                        Text(
//                            text = "Comment",
//                            color = Color.DarkGray,
//                            fontSize = 14.sp,
//                            modifier = Modifier.background(Color.Transparent)
//                        )
//                    },
//                    trailingIcon = {
//                        IconButton(
//                            onClick = {
//                                if (commentText.isNotBlank()) {
//                                    onCommentPost(commentText)
//                                    commentText = ""
//                                }
//                            }
//                        ) {
//                            Icon(
//                                imageVector = Icons.AutoMirrored.Filled.Send,
//                                contentDescription = "Post Comment"
//                            )
//                        }
//                    },
//                    textStyle = TextStyle(fontSize = 14.sp),
//                    keyboardOptions = KeyboardOptions.Default,
//                    onValueChange = { commentText = it },
//                    modifier = Modifier.fillMaxWidth(),
//                    shape = RoundedCornerShape(8.dp),
//                    colors = TextFieldDefaults.colors(
//                        focusedContainerColor = Color(0x1DF4F6F6),
//                        unfocusedContainerColor = Color(0xFFF6F6F6),
//                    ),
//
//                    )
//
//            }
//        }
//    }
//}
//


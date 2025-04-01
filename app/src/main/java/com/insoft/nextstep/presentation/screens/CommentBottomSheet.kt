package com.insoft.nextstep.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.insoft.nextstep.data.model.Comment

@OptIn(ExperimentalMaterial3Api::class)
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
            modifier = Modifier.heightIn(min = 200.dp, max = 400.dp)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                "Comments",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(comments.size) { pos ->
                    Text(
                        text = comments[pos].text,
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
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
                    label = {
                        Text(
                            text = "Comment",
                            color = Color.DarkGray,
                            fontSize = 14.sp,
                            modifier = Modifier.background(Color.Transparent)
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
                    onValueChange = { commentText = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0x1DF4F6F6),
                        unfocusedContainerColor = Color(0xFFF6F6F6),
                    ),

                    )

            }
        }
    }
}

//@Preview(showSystemUi = true)
//@Composable
//fun PreviewCommentBottomSheet() {
//    CommentBottomSheet(
//        videoId = "sample_video_id",
//        comments = listOf(
//            "Great video!",
//            "Loved it!",
//            "🔥🔥🔥",
//            "Great video!",
//            "Loved it!"
//        ),
//        onCommentPost = { comment -> println("Posted Comment: $comment") },
//        onDismiss = {}
//    )
//}

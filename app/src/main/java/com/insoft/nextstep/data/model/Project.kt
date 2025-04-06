import com.insoft.nextstep.data.model.CommentX
import com.insoft.nextstep.data.model.PostedBy

//data class Project(
//    val __v: Int,
//    val _id: String,
//    val comments: List<CommentX>,
//    val createdAt: String,
//    val description: String,
//    val image: String,
//    val postedBy: PostedBy,
//    val tags: List<String>,
//    val title: String,
//    val upvotes: List<String>
//)
data class Project(
    val id: String,
    val title: String,
    val description: String,
    val tags: List<String>,
    val image: String,
    val postedBy: Postedby,
    val upvotes: List<String>,
    val comments: List<Comments>,
    val createdAt: String,
    val githubLink: String,
    val liveLink: String,
    val techStack: List<String>
)

data class Postedby(
    val userId: String,
    val username: String,
    val profilePic: String
)

data class Comments(
    val userId: String,
    val username: String,
    val profilePic: String,
    val text: String,
    val createdAt: String
)

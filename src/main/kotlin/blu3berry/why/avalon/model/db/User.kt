package blu3berry.why.avalon.model.db

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "user")
data class User(
    var _id:String?,
    var username: String,
    var password: String,
    var email: String?,
    var salt: String?,
    var friends: MutableList<User>?,
)

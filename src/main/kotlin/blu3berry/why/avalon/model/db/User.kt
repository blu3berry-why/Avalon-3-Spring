package blu3berry.why.avalon.model.db

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId

@Document(collection = "user")
data class User(
    @MongoId
    val _id: ObjectId?,
    var username: String,
    var password: String,
    var email: String?,
    var salt: String?,
    var friends: MutableList<User>?,
)

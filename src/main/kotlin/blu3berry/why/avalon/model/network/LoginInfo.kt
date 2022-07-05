package blu3berry.why.avalon.model.network


data class LoginInfo(
    val username: String?,
    val password: String?,
    val email: String?,
    var friends: MutableList<LoginInfo>?,
)

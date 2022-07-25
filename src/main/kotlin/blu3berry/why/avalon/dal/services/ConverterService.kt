package blu3berry.why.avalon.dal.services

import blu3berry.why.avalon.model.db.User
import blu3berry.why.avalon.model.network.LoginInfo
import org.springframework.stereotype.Service

@Service
class ConverterService {

    fun toSingleLoginInfo(user: User) = LoginInfo(
            username = user.username,
            email = user.email,
            password = null,
            friends = null
        )
    fun toLoginInfo(user: User?): LoginInfo? {
        user ?: return null
        return LoginInfo(
        username = user.username,
        password = null,
        email = user.email,
        friends = user.friends?.map { toSingleLoginInfo(it) } ?.toMutableList()
    )}
}
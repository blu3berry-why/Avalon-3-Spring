package blu3berry.why.avalon.dal.interfaces

import blu3berry.why.avalon.model.db.User
import blu3berry.why.avalon.model.network.LoginInfo


interface IUserService {
    fun findUserByUsername(username : String) : User?
    fun updateUserByUsername(username : String, user: LoginInfo): User?
    fun deleteUserByUsername(username: String) : User?
}
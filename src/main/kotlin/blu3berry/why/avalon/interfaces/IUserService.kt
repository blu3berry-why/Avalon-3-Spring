package blu3berry.why.avalon.interfaces

import blu3berry.why.avalon.model.db.User

interface IUserService {
    fun findUserByUsername(username : String) : User?
    fun updateUserByUsername(username : String, user: User): User?
    fun deleteUserByUsername(username: String) : User?
}
package blu3berry.why.avalon.dal.services

import blu3berry.why.avalon.api.errorhandling.exceptions.ConflictException
import blu3berry.why.avalon.dal.interfaces.IUserService
import blu3berry.why.avalon.model.db.User
import blu3berry.why.avalon.dal.repository.UserRepository
import blu3berry.why.avalon.model.network.LoginInfo
import org.springframework.stereotype.Service

@Service
class UserService(var userRepository: UserRepository) : IUserService {

    override fun findUserByUsername(username: String) = userRepository.findUserByUsername(username)

    override fun updateUserByUsername(username: String, user: LoginInfo): User? {
        return findUserByUsername(username)?.also {
            it.username = user.username ?: it.username
            it.email = user.email ?: it.email
            //PASSWORDHASH
            //TODO()
            //it.password = hash(user.password, it.salt)
            val friends = user.friends?.map {
                findUserByUsername(it.username!!) ?:
                ConflictException.Throw("The given friend with the name: ${it.username} does not exist.")
            } ?.toMutableList()
            it.friends = friends ?: it.friends
            userRepository.save(it)
        }
    }

    override fun deleteUserByUsername(username: String) = userRepository.deleteByUsername(username)
}
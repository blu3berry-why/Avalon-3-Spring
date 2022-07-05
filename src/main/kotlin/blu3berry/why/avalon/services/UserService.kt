package blu3berry.why.avalon.services

import blu3berry.why.avalon.interfaces.IUserService
import blu3berry.why.avalon.model.db.User
import blu3berry.why.avalon.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(var userRepository: UserRepository) : IUserService {

    override fun findUserByUsername(username: String) = userRepository.findUserByUsername(username)

    override fun updateUserByUsername(username: String, user: User): User? {
        return findUserByUsername(username)?.let {
            it.username = user.username
            it.email = user.email
            //PASSWORDHASH
            //it.password = hash(user.password, it.salt)
            it.friends = user.friends
            userRepository.save(it)
        }
    }

    override fun deleteUserByUsername(username: String) = userRepository.deleteByUsername(username)
}
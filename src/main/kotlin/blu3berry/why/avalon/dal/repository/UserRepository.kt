package blu3berry.why.avalon.dal.repository

import blu3berry.why.avalon.model.db.User
import org.springframework.data.mongodb.repository.MongoRepository


interface UserRepository : MongoRepository<User, String>{
    fun findUserByUsername(username : String) : User?
    fun findUserBy_id(id: String): User?
    fun deleteByUsername(username : String) : User?
}
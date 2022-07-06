package blu3berry.why.avalon.api.controllers

import blu3berry.why.avalon.dal.services.interfaces.IUserService
import blu3berry.why.avalon.model.db.User
import blu3berry.why.avalon.dal.repository.UserRepository
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.websocket.server.PathParam

@RestController
class UserController(var userService: IUserService) {

    @GetMapping("user/{username}")
    fun getUserByUsername(@PathVariable username: String) = userService.findUserByUsername(username)

    @PutMapping("user/{username}")
    fun updateUserByUsername(@PathVariable username: String, @RequestBody user:User) = userService.updateUserByUsername(username, user)

    @DeleteMapping("user/{username}")
    fun deleteUserByUsername(@PathVariable username: String) = userService.deleteUserByUsername(username)
}
package blu3berry.why.avalon.controllers

import blu3berry.why.avalon.repository.UserRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import javax.websocket.server.PathParam

@RestController
class UserController(var userService: UserRepository) {

    @GetMapping("user/{username}")
    fun getUserByUsername(@PathVariable username: String) = userService.findUserByUsername(username)
}
package blu3berry.why.avalon.api.controllers

import blu3berry.why.avalon.dal.services.ConverterService
import blu3berry.why.avalon.dal.interfaces.IUserService
import blu3berry.why.avalon.model.network.LoginInfo
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("user/{username}")
class UserController(val userService: IUserService, val converterService: ConverterService) {

    @GetMapping
    fun getUserByUsername(@PathVariable username: String) = converterService.toLoginInfo(
        userService.findUserByUsername(username))


    //TODO only the user can update itself
    @PutMapping
    fun updateUserByUsername(@RequestHeader("Avalon-Header-Logged-In-User-Username") username: String, @RequestBody user: LoginInfo) = converterService.toLoginInfo(
        userService.updateUserByUsername(username, user))

    @DeleteMapping
    fun deleteUserByUsername(@RequestHeader("Avalon-Header-Logged-In-User-Username") username: String) = converterService.toLoginInfo(
        userService.deleteUserByUsername(username))
}
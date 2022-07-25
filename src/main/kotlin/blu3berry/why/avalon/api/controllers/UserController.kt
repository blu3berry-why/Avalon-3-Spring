package blu3berry.why.avalon.api.controllers

import blu3berry.why.avalon.dal.services.ConverterService
import blu3berry.why.avalon.dal.services.interfaces.IUserService
import blu3berry.why.avalon.model.network.LoginInfo
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("user/{username}")
class UserController(val userService: IUserService, val converterService: ConverterService) {

    @GetMapping
    fun getUserByUsername(@PathVariable username: String) = converterService.toLoginInfo(
        userService.findUserByUsername(username))

    @PutMapping
    fun updateUserByUsername(@PathVariable username: String, @RequestBody user: LoginInfo) = converterService.toLoginInfo(
        userService.updateUserByUsername(username, user))
    @DeleteMapping
    fun deleteUserByUsername(@PathVariable username: String) = converterService.toLoginInfo(
        userService.deleteUserByUsername(username))
}
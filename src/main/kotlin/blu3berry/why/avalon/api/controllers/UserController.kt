package blu3berry.why.avalon.api.controllers

import blu3berry.why.avalon.dal.services.ConverterService
import blu3berry.why.avalon.dal.interfaces.IUserService
import blu3berry.why.avalon.model.network.LoginInfo
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("user")
class UserController(val userService: IUserService, val converterService: ConverterService) {

    // GET looks up any user by name (path var). PUT/DELETE act on the authenticated
    // user via the gateway-injected header, so they carry no `{username}` segment — the
    // old `user/{username}` mapping on PUT/DELETE ignored the path value, which also
    // produced an invalid OpenAPI path param (undeclared) that broke kmpgen.
    @GetMapping("/{username}")
    fun getUserByUsername(@PathVariable username: String) = converterService.toLoginInfo(
        userService.findUserByUsername(username))


    @PutMapping
    fun updateUserByUsername(@RequestHeader("Avalon-Header-Logged-In-User-Username") username: String, @RequestBody user: LoginInfo) = converterService.toLoginInfo(
        userService.updateUserByUsername(username, user))

    @DeleteMapping
    fun deleteUserByUsername(@RequestHeader("Avalon-Header-Logged-In-User-Username") username: String) = converterService.toLoginInfo(
        userService.deleteUserByUsername(username))
}
package blu3berry.why.avalon.api.controllers

import blu3berry.why.avalon.model.network.Message
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import java.net.http.HttpHeaders

@RestController
class TestController {
    @GetMapping("/test")
    fun getTest() = Message("test message")

    @PostMapping("/test")
    fun postTest() = Message("test message")

    @GetMapping("/")
    fun running() = "Im running! :)"

    @GetMapping("/tryheader")
    fun getHeader(@RequestHeader("Avalon-Header-Logged-In-User-Username") username: String) = Message(username)
}
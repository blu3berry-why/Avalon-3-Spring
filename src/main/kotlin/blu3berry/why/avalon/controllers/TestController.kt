package blu3berry.why.avalon.controllers

import blu3berry.why.avalon.model.network.Message
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {
    @GetMapping("/test")
    fun getTest() = Message("test message")

    @PostMapping("/test")
    fun postTest() = Message("test message")

    @GetMapping("/")
    fun running() = "Im running! :)"

}
package blu3berry.why.avalon.controllers

import blu3berry.why.avalon.model.network.Settings
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

class OldMethodsController {
    @GetMapping("getinfo{lobbyid}")
    fun getLobbySettins(@PathVariable lobbyid:String): Settings {
        TODO()
    }
}
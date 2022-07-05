package blu3berry.why.avalon.controllers

import blu3berry.why.avalon.model.network.LobbyCode
import blu3berry.why.avalon.model.network.Message
import blu3berry.why.avalon.model.network.Settings
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class OldMethodsController(var lobbyController: LobbyController) {


    @GetMapping("getinfo{lobbyCode}")
    fun getLobbySettings(@PathVariable lobbyCode:String): Settings {
        return lobbyController.getLobbySettings(lobbyCode)
    }

    @GetMapping("/lobby/{lobbyCode}/playernames")
    fun getPlayerNames(@PathVariable lobbyCode: String):List<String>{
        return lobbyController.getPlayerNames(lobbyCode)
    }

    @PostMapping("/createlobby")
    fun createLobby(): LobbyCode {
        return lobbyController.createLobby()
    }

    @PostMapping("/join/{lobbyCode}")
    fun joinLobby(@PathVariable lobbyCode: String): Message {
        return lobbyController.joinLobby(lobbyCode)
    }

    @PostMapping("/leave/{lobbyCode}")
    fun leaveLobby(@PathVariable lobbyCode: String): Message {
        return lobbyController.leaveLobby(lobbyCode)
    }

    @PostMapping("/start/{lobbyCode}")
    fun startLobby(@PathVariable lobbyCode: String): Message {
        return lobbyController.startLobby(lobbyCode)
    }

    @PutMapping("/settings/{lobbyCode}")
    fun updateSettings(@PathVariable lobbyCode: String): Message {
        return lobbyController.updateSettings(lobbyCode)
    }

}
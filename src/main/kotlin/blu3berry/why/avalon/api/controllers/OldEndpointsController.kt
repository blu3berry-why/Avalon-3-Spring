package blu3berry.why.avalon.api.controllers

import blu3berry.why.avalon.model.network.LobbyCode
import blu3berry.why.avalon.model.network.Message
import blu3berry.why.avalon.model.network.Settings
import org.springframework.web.bind.annotation.*

@RestController
class OldEndpointsController(val lobbyController: LobbyController) {


    @GetMapping("getinfo/{lobbyCode}")
    fun getLobbySettings(@PathVariable lobbyCode:String): Settings
        = lobbyController.getLobbySettings(lobbyCode)


    @GetMapping("/getplayers/{lobbyCode}/")
    fun getPlayerNames(@PathVariable lobbyCode: String):List<String>{
        return lobbyController.getPlayerNames(lobbyCode)
    }

    @PostMapping("/createlobby")
    fun createLobby(): LobbyCode {
        return lobbyController.createLobby()
    }

    @PostMapping("/join/{lobbyCode}")
    fun joinLobby(@PathVariable lobbyCode: String, @RequestHeader("Avalon-Header-Logged-In-User-Username") username: String): Message {
        return lobbyController.joinLobby(lobbyCode, username)
    }

    @PostMapping("/leave/{lobbyCode}")
    fun leaveLobby(@PathVariable lobbyCode: String, @RequestHeader("Avalon-Header-Logged-In-User-Username") username: String): Message {
        return lobbyController.leaveLobby(lobbyCode, username)
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
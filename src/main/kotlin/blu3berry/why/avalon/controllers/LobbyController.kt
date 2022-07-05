package blu3berry.why.avalon.controllers

import blu3berry.why.avalon.model.network.LobbyCode
import blu3berry.why.avalon.model.network.Message
import blu3berry.why.avalon.model.network.Settings
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class LobbyController {
    @GetMapping("/lobby/{lobbyCode}/settings")
    fun getLobbySettings(@PathVariable lobbyCode: String): Settings {
        TODO()
    }

    @GetMapping("/lobby/{lobbyCode}/playernames")
    fun getPlayerNames(@PathVariable lobbyCode: String):List<String>{
        TODO()
    }

    @PostMapping("/lobby/create")
    fun createLobby():LobbyCode{
        TODO()
    }

    @PostMapping("/lobby/{lobbyCode}/join")
    fun joinLobby(@PathVariable lobbyCode: String): Message {
        TODO()
    }

    @PostMapping("/lobby/{lobbyCode}/leave")
    fun leaveLobby(@PathVariable lobbyCode: String): Message{
        TODO()
    }

    @PostMapping("/lobby/{lobbyCode}/start")
    fun startLobby(@PathVariable lobbyCode: String): Message{
        TODO()
    }

    @PutMapping("/lobby/{lobbyCode}/settings")
    fun updateSettings(@PathVariable lobbyCode: String): Message{
        TODO()
    }



}
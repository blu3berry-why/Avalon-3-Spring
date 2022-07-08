package blu3berry.why.avalon.api.controllers

import blu3berry.why.avalon.model.network.LobbyCode
import blu3berry.why.avalon.model.network.Message
import blu3berry.why.avalon.model.network.Settings
import blu3berry.why.avalon.dal.services.LobbyService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class LobbyController(val lobbyService: LobbyService) {
    @GetMapping("/lobby/{lobbyCode}/settings")
    fun getLobbySettings(@PathVariable lobbyCode: String): Settings {
        return lobbyService.getLobbySettings(lobbyCode) ?: throw  ResponseStatusException(
                HttpStatus.NOT_FOUND, "Lobby not found"
        )
    }

    @GetMapping("/lobby/{lobbyCode}/playernames")
    fun getPlayerNames(@PathVariable lobbyCode: String):List<String>{
        return lobbyService.getPlayerNames(lobbyCode) ?: throw  ResponseStatusException(
                HttpStatus.NOT_FOUND, "Lobby not found"
        )
    }

    @PostMapping("/lobby/create")
    fun createLobby():LobbyCode = LobbyCode(lobbyService.createLobby().lobbyCode)


    @PostMapping("/lobby/{lobbyCode}/join")
    fun joinLobby(@PathVariable lobbyCode: String): Message {
        return if (lobbyService.joinLobby(lobbyCode,TODO())){
            Message("Join successful")
        }else{
            Message("Join failed")
        }
    }

    @PostMapping("/lobby/{lobbyCode}/leave")
    fun leaveLobby(@PathVariable lobbyCode: String): Message{
        return if (lobbyService.leaveLobby(lobbyCode,TODO())){
            Message("Leave successful")
        }else{
            Message("Leave failed")
        }
    }

    @PostMapping("/lobby/{lobbyCode}/start")
    fun startLobby(@PathVariable lobbyCode: String): Message{
        lobbyService.startLobby(lobbyCode)
        return Message.OK
    }

    @PutMapping("/lobby/{lobbyCode}/settings")
    fun updateSettings(@PathVariable lobbyCode: String): Message{
        TODO()
    }



}
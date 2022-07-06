package blu3berry.why.avalon.api.controllers

import blu3berry.why.avalon.model.network.*
import blu3berry.why.avalon.dal.services.GameService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class GameController(val gameService: GameService) {
    @GetMapping("/game/{lobbyCode}")
    fun getGameInfo(@PathVariable lobbyCode:String): Info{
        return gameService.getGameInfo(lobbyCode) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby not found")
    }

    @GetMapping("/game/{lobbyCode}/character")
    fun getCharacter(@PathVariable lobbyCode: String): CharacterInfo{
        TODO()
    }

    @PostMapping("/game/{lobbyCode}/select")
    fun selectForAdventure(@PathVariable lobbyCode: String, @RequestBody list: List<String>): Message{
        TODO()
    }

    @PostMapping("/game/{lobbyCode}/vote")
    fun vote(@PathVariable lobbyCode: String, @RequestBody vote:SingleVote): Message{
        TODO()
    }

    @PostMapping("/game/{lobbyCode}/adventureVote")
    fun adventure(@PathVariable lobbyCode: String, @RequestBody vote: SingleVote): Message{
        TODO()
    }

    @PostMapping("/game/{lobbyCode}/assassin")
    fun assassin(@PathVariable lobbyCode: String, @RequestBody guess:AssassinGuess): Message{
        TODO()
    }


}
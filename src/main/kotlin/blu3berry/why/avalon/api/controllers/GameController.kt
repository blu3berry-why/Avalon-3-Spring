package blu3berry.why.avalon.api.controllers

import blu3berry.why.avalon.model.network.*

import blu3berry.why.avalon.dal.services.interfaces.IGameService

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("game")
class GameController(val gameService: IGameService) {
    @GetMapping("/{lobbyCode}")
    fun getGameInfo(@PathVariable lobbyCode: String): Info =
        gameService.getGameInfo(lobbyCode)

    @GetMapping("/{lobbyCode}/character")
    fun getCharacter(@PathVariable lobbyCode: String, @RequestBody username: String): CharacterInfo =
        gameService.getCharacter(lobbyCode, username)

    @PostMapping("/{lobbyCode}/select")
    fun selectForAdventure(@PathVariable lobbyCode: String, @RequestBody selected: List<String>): Message =
        gameService.select(lobbyCode, selected)


    @PostMapping("/{lobbyCode}/vote")
    fun vote(@PathVariable lobbyCode: String, @RequestBody vote: SingleVote): Message =
        gameService.vote(lobbyCode, vote)

    @PostMapping("/{lobbyCode}/adventureVote")
    fun adventure(@PathVariable lobbyCode: String, @RequestBody vote: SingleVote): Message =
        gameService.adventureVote(lobbyCode, vote)


    @PostMapping("/{lobbyCode}/assassin")
    fun assassin(@PathVariable lobbyCode: String, @RequestBody guess: AssassinGuess): Message =
        gameService.guess(lobbyCode, guess)


}
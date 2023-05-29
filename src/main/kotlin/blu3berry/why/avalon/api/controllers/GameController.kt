package blu3berry.why.avalon.api.controllers

import blu3berry.why.avalon.dal.interfaces.IGameService
import blu3berry.why.avalon.model.network.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("game")
class GameController(val gameService: IGameService) {
    @GetMapping("/{lobbyCode}")
    fun getGameInfo(@PathVariable lobbyCode: String): Info =
        gameService.getGameInfo(lobbyCode)


    //TODO változtattamm erról: @RequestBody username: String check if good still
    @GetMapping("/{lobbyCode}/character")
    fun getCharacter(@PathVariable lobbyCode: String, @RequestHeader("Avalon-Header-Logged-In-User-Username") username: String): CharacterInfo =
        gameService.getCharacter(lobbyCode, username)

    @PostMapping("/{lobbyCode}/select")
    fun selectForAdventure(@PathVariable lobbyCode: String, @RequestBody selected: List<String>,@RequestHeader("Avalon-Header-Logged-In-User-Username") username: String): Message =
        gameService.select(lobbyCode, selected, username)


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
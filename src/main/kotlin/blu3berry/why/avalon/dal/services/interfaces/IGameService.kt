package blu3berry.why.avalon.dal.services.interfaces

import blu3berry.why.avalon.model.network.*

interface IGameService {
    fun getGameInfo(lobbyCode: String): Info

    fun getCharacter(lobbyCode: String, username: String): CharacterInfo

    fun select(lobbyCode: String, selected: List<String>, username: String): Message

    fun vote(lobbyCode: String, vote: SingleVote): Message

    fun adventureVote(lobbyCode: String, vote: SingleVote): Message

    fun guess(lobbyCode: String, guess: AssassinGuess): Message
}
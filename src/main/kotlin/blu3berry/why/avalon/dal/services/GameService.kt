package blu3berry.why.avalon.dal.services

import blu3berry.why.avalon.api.errorhandling.exceptions.ResourceNotFoundException
import blu3berry.why.avalon.dal.extensions.sees
import blu3berry.why.avalon.dal.extensions.select
import blu3berry.why.avalon.dal.extensions.vote
import blu3berry.why.avalon.dal.extensions.voteOnAdventure
import blu3berry.why.avalon.model.db.UserRoleMap
import blu3berry.why.avalon.model.network.*
import blu3berry.why.avalon.dal.repository.LobbyRepository
import blu3berry.why.avalon.dal.services.interfaces.IGameService
import blu3berry.why.avalon.dal.services.interfaces.ILobbyOperations
import org.springframework.stereotype.Service

@Service
class GameService(override val lobbyRepository: LobbyRepository) : ILobbyOperations, IGameService {

    override fun getGameInfo(lobbyCode: String): Info {
        return lobbyByCode(lobbyCode).info
    }

    override fun getCharacter(lobbyCode: String, username: String): CharacterInfo {
        val lobby = lobbyByCode(lobbyCode)
        val role = lobby
            .userRoles
            .first { userRoleMap: UserRoleMap -> userRoleMap.userName == username }
            .role
        return CharacterInfo(role, lobby.sees(role, username))
    }

    override fun select(lobbyCode: String, selected: List<String>): Message {
        lobbyByCode(lobbyCode).select(selected)
        return Message.OK
    }

    override fun vote(lobbyCode: String, vote: SingleVote): Message {
        lobbyByCode(lobbyCode).vote(vote)
        return Message.OK
    }

    override fun adventureVote(lobbyCode: String, vote: SingleVote): Message {
        lobbyByCode(lobbyCode).voteOnAdventure(vote)
        return Message.OK
    }

    override fun guess(lobbyCode: String, guess: AssassinGuess): Message {
        TODO()
    }


}
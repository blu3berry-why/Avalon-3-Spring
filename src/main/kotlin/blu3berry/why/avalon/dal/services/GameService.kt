package blu3berry.why.avalon.dal.services


import blu3berry.why.avalon.dal.interfaces.IGameService
import blu3berry.why.avalon.dal.interfaces.ILobbyOperations
import blu3berry.why.avalon.dal.repository.LobbyRepository
import blu3berry.why.avalon.model.db.UserRoleMap
import blu3berry.why.avalon.model.db.lobby.extensions.sees
import blu3berry.why.avalon.model.db.lobby.extensions.select
import blu3berry.why.avalon.model.db.lobby.extensions.vote
import blu3berry.why.avalon.model.db.lobby.extensions.voteOnAdventure
import blu3berry.why.avalon.model.enums.WINNER
import blu3berry.why.avalon.model.network.*
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

    override fun select(lobbyCode: String, selected: List<String>, username: String): Message {
        lobbyByCode(lobbyCode).let {
            it.select(selected, username)
            lobbyRepository.save(it)
        }
        return Message.OK
    }

    override fun vote(lobbyCode: String, vote: SingleVote): Message {
        lobbyByCode(lobbyCode).let {
            it.vote(vote)
            lobbyRepository.save(it)
        }
        return Message.OK
    }

    override fun adventureVote(lobbyCode: String, vote: SingleVote): Message {
        lobbyByCode(lobbyCode).let {
            it.voteOnAdventure(vote)
            lobbyRepository.save(it)
        }
        return Message.OK
    }

    override fun guess(lobbyCode: String, guess: AssassinGuess): Message {
        val lobby = lobbyByCode(lobbyCode)
        val guessRole = lobby.userRoles.first { it.userName == guess.guess }.role

        if (guessRole.isMerlin)
            lobby.info.winner = WINNER.EVIL
        else
            lobby.info.winner = WINNER.GOOD

        lobbyRepository.save(lobby)
        return Message.OK

    }




}
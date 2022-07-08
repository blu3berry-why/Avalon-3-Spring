package blu3berry.why.avalon.dal.services

import blu3berry.why.avalon.api.errorhandling.exceptions.ResourceNotFoundException
import blu3berry.why.avalon.dal.extensions.start
import blu3berry.why.avalon.model.helpers.Constants
import blu3berry.why.avalon.model.db.Lobby
import blu3berry.why.avalon.model.enums.SCORE
import blu3berry.why.avalon.model.network.Info
import blu3berry.why.avalon.model.network.Settings
import blu3berry.why.avalon.dal.repository.LobbyRepository
import blu3berry.why.avalon.dal.services.interfaces.ILobbyOperations
import blu3berry.why.avalon.dal.services.interfaces.ILobbyService
import org.springframework.stereotype.Service


@Service
class LobbyService(
    val constants: Constants,
    val randomizeService: RandomizeService,
    override val lobbyRepository: LobbyRepository
    )
    : ILobbyOperations, ILobbyService
{

    override fun getLobbySettings(lobbyCode: String): Settings = lobbyByCode(lobbyCode).settings

    override fun getPlayerNames(lobbyCode: String): MutableList<String> = lobbyByCode(lobbyCode).info.playersName

    override fun joinLobby(lobbyCode: String, username: String) : Boolean{
        val lobby = lobbyByCode(lobbyCode)
        if (!lobby.info.playersName.contains(username)){
            lobby.info.playersName.add(username)
        }
        return true
    }

    override fun leaveLobby(lobbyCode: String, username: String) : Boolean{
        val lobby = lobbyByCode(lobbyCode)
        if (!lobby.info.playersName.contains(username)){
            lobby.info.playersName.remove(username)
        }
        return true
    }

    override fun startLobby(lobbyCode: String){
        return lobbyByCode(lobbyCode).start()
    }

    override fun updateSettings(lobbyCode: String, settings: Settings){
        lobbyByCode(lobbyCode).apply {
            this.settings = settings
            lobbyRepository.save(this)
        }
    }

    override fun createLobby(): Lobby {
        return Lobby(
            lobbyCode = randomizeService.sixCharStr(),
            info = Info(
                started = false,
                scores = mutableListOf(SCORE.UNDECIDED, SCORE.UNDECIDED, SCORE.UNDECIDED, SCORE.UNDECIDED, SCORE.UNDECIDED),
                currentRound = 0,
                isAdventure = false,
                currentAdventure = 0,
                king = null,
                selectedForAdventure = mutableListOf(),
                playersName = mutableListOf(),
            ),
            settings = Settings(
                assassin = false,
                mordred = false,
                morgana = false,
                oberon = false,
                percival = false,
                arnold = false,
            ),
            votes = mutableListOf(),
            userRoles = mutableListOf(),
            adventureVotes = mutableListOf(),
        )
    }
}
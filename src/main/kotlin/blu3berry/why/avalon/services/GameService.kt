package blu3berry.why.avalon.services

import blu3berry.why.avalon.model.network.Info
import blu3berry.why.avalon.repository.LobbyRepository
import org.springframework.stereotype.Service

@Service
class GameService(val lobbyRepository: LobbyRepository) {
    fun getGameInfo(lobbyCode:String): Info?{
        return lobbyRepository.findLobbyByLobbyCode(lobbyCode)?.info
    }
}
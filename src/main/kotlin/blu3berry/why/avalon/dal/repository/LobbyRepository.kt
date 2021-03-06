package blu3berry.why.avalon.dal.repository

import blu3berry.why.avalon.model.db.lobby.Lobby
import org.springframework.data.mongodb.repository.MongoRepository

interface LobbyRepository : MongoRepository<Lobby, String>{
    fun findLobbyByLobbyCode(lobbyCode : String) : Lobby?
}
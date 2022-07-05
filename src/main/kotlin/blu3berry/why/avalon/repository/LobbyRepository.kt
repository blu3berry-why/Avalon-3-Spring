package blu3berry.why.avalon.repository

import blu3berry.why.avalon.model.db.Lobby
import org.springframework.data.mongodb.repository.MongoRepository

interface LobbyRepository : MongoRepository<Lobby, String>{
    fun findLobbyByLobbyCode(lobbyCode : String) : Lobby?
}
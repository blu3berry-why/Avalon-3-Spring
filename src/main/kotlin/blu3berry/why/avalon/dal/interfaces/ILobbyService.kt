package blu3berry.why.avalon.dal.interfaces

import blu3berry.why.avalon.model.db.lobby.Lobby
import blu3berry.why.avalon.model.network.Settings

interface ILobbyService {
    fun getLobbySettings(lobbyCode: String): Settings

    fun getPlayerNames(lobbyCode: String): MutableList<String>

    fun joinLobby(lobbyCode: String, username: String) : Boolean

    fun leaveLobby(lobbyCode: String, username: String) : Boolean

    fun startLobby(lobbyCode: String)

    fun updateSettings(lobbyCode: String, settings: Settings)

    fun createLobby(): Lobby
}
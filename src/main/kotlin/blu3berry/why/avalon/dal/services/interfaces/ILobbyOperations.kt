package blu3berry.why.avalon.dal.services.interfaces

import blu3berry.why.avalon.api.errorhandling.exceptions.ResourceNotFoundException
import blu3berry.why.avalon.dal.repository.LobbyRepository

interface ILobbyOperations {

    val lobbyRepository: LobbyRepository

    fun lobbyByCode(lobbyCode: String) = lobbyRepository.findLobbyByLobbyCode(lobbyCode) ?: ResourceNotFoundException.lobbyNotFound
}
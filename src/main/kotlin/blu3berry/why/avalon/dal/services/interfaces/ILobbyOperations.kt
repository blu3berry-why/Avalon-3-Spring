package blu3berry.why.avalon.dal.services.interfaces

import blu3berry.why.avalon.api.errorhandling.exceptions.ResourceNotFoundException
import blu3berry.why.avalon.dal.services.repository.LobbyRepository
import java.util.*

interface ILobbyOperations {

    val lobbyRepository: LobbyRepository

    fun lobbyByCode(lobbyCode: String) = lobbyRepository.findLobbyByLobbyCode(lobbyCode.lowercase(Locale.getDefault())) ?: ResourceNotFoundException.lobbyNotFound

}
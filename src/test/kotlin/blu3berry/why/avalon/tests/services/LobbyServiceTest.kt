package blu3berry.why.avalon.tests.services


import blu3berry.why.avalon.dal.services.LobbyService
import blu3berry.why.avalon.dal.services.RandomizeService
import blu3berry.why.avalon.dal.repository.LobbyRepository
import blu3berry.why.avalon.mockdata.builders.LobbyBuilder
import blu3berry.why.avalon.model.network.Settings
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class LobbyServiceTest {


    private lateinit var lobbyService: LobbyService

    @Mock
    private lateinit var lobbyRepository: LobbyRepository
    @Mock
    private lateinit var randomizeService: RandomizeService


    @BeforeEach
    fun setup(){
        MockitoAnnotations.openMocks(this)
        lobbyService = LobbyService(randomizeService,lobbyRepository)
    }
    @Test
    fun `test getLobbySettings returns correct settings`() {
        // Arrange
        val lobbyCode = "lobby123"
        val settings = Settings(assassin = true, mordred = false, morgana = true, oberon = false, percival = false, arnold = false)
        val lobby = LobbyBuilder(lobbyCode)
            .apply {
            this.settings = settings
        }.
        build()
        `when`(lobbyRepository.findLobbyByLobbyCode(lobbyCode)).thenReturn(lobby)

        // Act
        val result = lobbyService.getLobbySettings(lobbyCode)

        // Assert
        assertEquals(settings, result)
    }

    @Test
    fun `test getPlayerNames returns correct player names`() {
        // Arrange
        val lobbyCode = "lobby123"
        val playerNames = mutableListOf("player1", "player2", "player3")
        val lobby = LobbyBuilder(lobbyCode)
            .info {
                this.playersName = playerNames
            }

            .build()
        `when`(lobbyRepository.findLobbyByLobbyCode(lobbyCode)).thenReturn(lobby)

        // Act
        val result = lobbyService.getPlayerNames(lobbyCode)

        // Assert
        assertEquals(playerNames, result)
    }

    @Test
    fun `test joinLobby adds player name to lobby`() {
        // Arrange
        val lobbyCode = "lobby123"
        val username = "player4"
        val lobby = LobbyBuilder(lobbyCode)
            .build()
        `when`(lobbyRepository.findLobbyByLobbyCode(lobbyCode)).thenReturn(lobby)

        // Act
        val result = lobbyService.joinLobby(lobbyCode, username)

        // Assert
        assertTrue(result)
        assertTrue(lobby.info.playersName.contains(username))
        verify(lobbyRepository).save(lobby)
    }

    @Test
    fun `test joinLobby player already in lobby`() {
        // Arrange
        val lobbyCode = "lobby123"
        val username = "player4"
        val lobby = LobbyBuilder(lobbyCode)
            .info{
                playersName = mutableListOf("player4")
            }
            .build()
        `when`(lobbyRepository.findLobbyByLobbyCode(lobbyCode)).thenReturn(lobby)

        // Act
        val result = lobbyService.joinLobby(lobbyCode, username)

        // Assert
        assertTrue(result)
        // Has to have it only once
        assertEquals(1, lobby.info.playersName.filter { it == username }.size)
        verify(lobbyRepository).save(lobby)
    }

  @Test
    fun `test leaveLobby removes player name from lobby`() {
        // Arrange
        val lobbyCode = "lobby123"
        val username = "player2"
        val playerNames = mutableListOf("player1", "player2", "player3")
        val lobby = LobbyBuilder(lobbyCode)
            .info {
          this.playersName = playerNames
                }
            .build()
      `when`(lobbyRepository.findLobbyByLobbyCode(lobbyCode)).thenReturn(lobby)

        // Act
        val result = lobbyService.leaveLobby(lobbyCode, username)

        // Assert
        assertTrue(result)
        assertFalse(lobby.info.playersName.contains(username))
        verify(lobbyRepository).save(lobby)
    }

    @Test
    fun `test leaveLobby player not in lobby`() {
        // Arrange
        val lobbyCode = "lobby123"
        val username = "player2"
        val playerNames = mutableListOf("player1", "player3")
        val lobby = LobbyBuilder(lobbyCode)
            .info {
                this.playersName = playerNames
            }
            .build()
        `when`(lobbyRepository.findLobbyByLobbyCode(lobbyCode)).thenReturn(lobby)

        // Act
        val result = lobbyService.leaveLobby(lobbyCode, username)

        // Assert
        assertTrue(result)
        assertFalse(lobby.info.playersName.contains(username))
        verify(lobbyRepository).save(lobby)
    }


}

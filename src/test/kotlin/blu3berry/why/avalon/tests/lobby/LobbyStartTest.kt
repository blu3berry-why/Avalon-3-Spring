package blu3berry.why.avalon.tests.lobby

import blu3berry.why.avalon.api.errorhandling.exceptions.ConflictException
import blu3berry.why.avalon.mockdata.builders.LobbyBuilder
import blu3berry.why.avalon.model.db.lobby.extensions.start
import blu3berry.why.avalon.model.helpers.Constants
import com.mongodb.assertions.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LobbyStartTest {

    @Test
    fun `start should throw an exception if the lobby has already started`() {
        val lobby = LobbyBuilder("lobby_start_test")
            .info {
                started = true
            }
            .build()

        assertThrows<ConflictException> {
            lobby.start()
        }
    }

    @Test
    fun `start should throw an exception if there are too few players`() {
        val lobby = LobbyBuilder("lobby_start_test")
            .info {
                playersName = mutableListOf("Player1", "Player2", "Player3")
            }
            .build()

        assertThrows<ConflictException> {
            lobby.start()
        }
    }

    @Test
    fun `start should throw an exception if there are too many players`() {
        val lobby = LobbyBuilder("lobby_start_test")
            .info {
                playersName = mutableListOf(
                    "Player1", "Player2", "Player3", "Player4", "Player5",
                    "Player6", "Player7", "Player8", "Player9", "Player10", "Player11"
                )
            }
            .build()

        assertThrows<ConflictException> {
            lobby.start()
        }
    }

    @Test
    fun `start should successfully start the lobby and assign roles to players`() {
        val lobby = LobbyBuilder("lobby_start_test")
            .info {
                started = false
                playersName = mutableListOf("Player1", "Player2", "Player3", "Player4", "Player5")
            }
            // Getting out the builders placeholders
            .apply {
                votes = mutableListOf()
                adventureVotes = mutableListOf()
            }
            .build()

        lobby.start()

        // Verify the expected behavior and outcomes of the function
        // Assert that the lobby is marked as started
        assertTrue(lobby.info.started)

        // Assert that the king is set to the first player
        assertEquals("Player1", lobby.info.king)

        // Assert that the current round is set to 1
        assertEquals(1, lobby.info.currentRound)

        // Assert that the player select number is set based on the player size and round
        assertEquals(Constants.adventureLimit[5].limits[1], lobby.info.playerSelectNum)

        // Assert that the votes and adventureVotes lists are initialized with placeholders
        assertEquals(2, lobby.votes.size)
        assertEquals(1, lobby.adventureVotes.size)

        // Assert that the user roles are assigned correctly
        assertEquals(5, lobby.userRoles.size)
        // Additional assertions can be made based on the expected role assignments
    }
}
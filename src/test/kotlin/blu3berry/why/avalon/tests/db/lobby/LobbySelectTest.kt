package blu3berry.why.avalon.tests.db.lobby

import blu3berry.why.avalon.api.errorhandling.exceptions.ConflictException
import blu3berry.why.avalon.mockdata.builders.LobbyBuilder
import blu3berry.why.avalon.model.db.lobby.extensions.select
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class LobbySelectTest {
    @Test
    fun `select should choose players for the adventure when called by the king`() {
        val lobby = LobbyBuilder("lobby_select_test")
            .info {
                king = "Player1"
                currentRound = 1
                playersName = mutableListOf("Player1", "Player2", "Player3", "Player4", "Player5", )
                selectedForAdventure = mutableListOf()
            }
            .addVote { king = "Player1" }
            .build()

        val chosenPlayers = listOf("Player2", "Player3")

        // Invoke the select function as the king
        lobby.select(chosenPlayers, "Player1")

        // Verify the expected behavior and outcomes of the function
        // Assert that the chosen players are added to the current adventure selection
        assertEquals(chosenPlayers, lobby.info.selectedForAdventure)
        assertEquals(chosenPlayers, lobby.votes[1].chosen)
    }

    @Test
    fun `select should throw an exception when called by a non-king player`() {
        val lobby = LobbyBuilder("lobby_select_test")
            .info {
                king = "Player1"
                currentRound = 1
                playersName = mutableListOf("Player1", "Player2", "Player3", "Player4", "Player5", )
                selectedForAdventure = mutableListOf()
            }
            .build()

        val chosenPlayers = listOf("Player2", "Player3")

        // Invoke the select function as a non-king player
        assertThrows<ConflictException> {
            lobby.select(chosenPlayers, "Player2")
        }
    }

    @Test
    fun `select should throw an exception when there are repeated usernames in the chosen players`() {
        val lobby = LobbyBuilder("lobby_select_test")
            .info {
                king = "Player1"
                currentRound = 1
                playersName = mutableListOf("Player1", "Player2", "Player3", "Player4", "Player5", )
                selectedForAdventure = mutableListOf()
            }
            .build()

        val chosenPlayers = listOf("Player2", "Player2") // Player2 is repeated

        // Invoke the select function with repeated usernames
        val exception = assertThrows<ConflictException> {
            lobby.select(chosenPlayers, "Player1")
        }

        // Verify the expected behavior and outcomes of the function
        // Assert that the exception message indicates repeated usernames
        assertEquals("Repeated usernames!", exception.message)
    }

    @Test
    fun `select should throw an exception when the number of chosen players is incorrect`() {
        val lobby = LobbyBuilder("lobby_select_test")
            .info {
                king = "Player1"
                currentRound = 1
                playersName = mutableListOf("Player1", "Player2", "Player3", "Player4", "Player5", )
                selectedForAdventure = mutableListOf()
            }
            .build()

        val chosenPlayers = listOf("Player2", "Player3", "Player4") // Incorrect number of players

        // Invoke the select function with an incorrect number of chosen players
        val exception = assertThrows<ConflictException> {
            lobby.select(chosenPlayers, "Player1")
        }

        // Verify the expected behavior and outcomes of the function
        // Assert that the exception message indicates the incorrect number of people
        assertEquals(
            "This is not the required amount of people! Required: 2, but found: 3!",
            exception.message
        )
    }

    @Test
    fun `select should throw an exception when the king has already chosen`() {
        val lobby = LobbyBuilder("lobby_select_test")
            .info {
                king = "Player1"
                currentRound = 1
                playersName = mutableListOf("Player1", "Player2", "Player3", "Player4", "Player5", )
                selectedForAdventure = mutableListOf()
            }
            .addVote {
                king = "Player1"
            }
            .build()

        val chosenPlayers = listOf("Player2", "Player3")

        // Simulate the king already making a selection
        lobby.select(chosenPlayers, "Player1")

        // Invoke the select function again
        val exception = assertThrows<ConflictException> {
            lobby.select(chosenPlayers, "Player1")
        }

        // Verify the expected behavior and outcomes of the function
        // Assert that the exception message indicates that the king has already chosen
        assertEquals("The king has already chosen", exception.message)
    }

}
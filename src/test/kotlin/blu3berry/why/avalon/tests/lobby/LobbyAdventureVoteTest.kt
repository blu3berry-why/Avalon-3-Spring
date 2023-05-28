package blu3berry.why.avalon.tests.lobby

import blu3berry.why.avalon.api.errorhandling.exceptions.ConflictException
import blu3berry.why.avalon.mockdata.builders.LobbyBuilder
import blu3berry.why.avalon.model.db.lobby.extensions.voteOnAdventure
import blu3berry.why.avalon.model.enums.SCORE
import blu3berry.why.avalon.model.enums.WINNER
import blu3berry.why.avalon.model.network.SingleVote
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LobbyAdventureVoteTest {


    @Test
    fun `voteOnAdventure should throw an exception when there is no adventure going on`() {
        // Create a test instance of the Lobby class or mock the necessary dependencies
        val lobby = LobbyBuilder("lobby_voteOnAdventure")
            .info {
                isAdventure = false
            }
            .build()


        // Invoke the voteOnAdventure function
        val vote = SingleVote(username = "Player1", uservote = true)

        // Verify the expected behavior and outcomes of the function
        // Assert that the function throws an exception indicating that there is no adventure going on
        assertThrows(ConflictException::class.java) {
            lobby.voteOnAdventure(vote)
        }
    }

    @Test
    fun `voteOnAdventure should throw an exception when the player is not chosen`() {
        val currentAdventure = 1

        // Create a test instance of the Lobby class
        // Set up the necessary data for the test
        val lobby = LobbyBuilder("lobby_vote_on_adventure_test")
            .info {
                isAdventure = true
                this.currentAdventure = currentAdventure
                playersName = mutableListOf("Player1", "Player2", "Player3", "Player4", "Player5")
            }
            .addAdventureVote {
                king = "Player1"
                chosen = mutableListOf("Player2", "Player3")
                results = mutableListOf()
            }
            .build()

        // Invoke the voteOnAdventure function for a player who is not chosen
        val vote = SingleVote(username = "Player1", uservote = true)

        // Verify the expected behavior and outcomes of the function
        // Assert that the function throws an exception indicating that the player is not chosen
        assertThrows<ConflictException> {
            lobby.voteOnAdventure(vote)
        }
    }

    @Test
    fun `voteOnAdventure should throw an exception when the player has already voted`() {
        val currentAdventure = 1

        // Create a test instance of the Lobby class
        // Set up the necessary data for the test
        val lobby = LobbyBuilder("lobby_vote_on_adventure_test")
            .info {
                isAdventure = true
                this.currentAdventure = currentAdventure
                playersName = mutableListOf("Player1", "Player2", "Player3", "Player4", "Player5")
            }
            .addAdventureVote {
                king = "Player1"
                chosen = mutableListOf("Player1", "Player2")
                results = mutableListOf(SingleVote("Player1", true))
            }
            .build()

        // Invoke the voteOnAdventure function for a player who has already voted
        val vote = SingleVote(username = "Player1", uservote = true)

        // Verify the expected behavior and outcomes of the function
        // Assert that the function throws an exception indicating that the player has already voted
        assertThrows<ConflictException> {
            lobby.voteOnAdventure(vote)
        }
    }

    @Test
    fun `voteOnAdventure should update scores when a player votes successfully`() {
        val currentAdventure = 1

        // Create a test instance of the Lobby class
        // Set up the necessary data for the test
        val lobby = LobbyBuilder("lobby_vote_on_adventure_test")
            .info {
                isAdventure = true
                this.currentAdventure = currentAdventure
                playersName = mutableListOf("Player1", "Player2", "Player3", "Player4", "Player5")
            }
            .addAdventureVote {
                king = "Player1"
                chosen = mutableListOf("Player1", "Player2")
                results = mutableListOf()
            }
            .build()

        // Invoke the voteOnAdventure function for a player who votes successfully
        val vote = SingleVote(username = "Player1", uservote = true)
        lobby.voteOnAdventure(vote)

        // Verify the expected behavior and outcomes of the function
        // Assert that the player's vote is added to the results list
        assertEquals(1, lobby.adventureVotes[currentAdventure].results.size)
        assertEquals(vote, lobby.adventureVotes[currentAdventure].results.first())
    }




    @Test
    fun `voteOnAdventure should update scores and progress to next round when all players vote(success) successfully`() {
        val currentAdventure = 1

        // Create a test instance of the Lobby class
        // Set up the necessary data for the test
        val lobby = LobbyBuilder("lobby_vote_on_adventure_test")
            .info{
                king = "Player1"
                this.
                currentRound= 1
                this.currentAdventure = currentAdventure
                this.isAdventure = true
                playerSelectNum = 2
                playersName = mutableListOf("Player1", "Player2","Player3", "Player4","Player5")

            }
            .addAdventureVote {
                king = "Player1"
                chosen = mutableListOf("Player1", "Player2")
                results = mutableListOf()
            }
            .apply {
                selectionRound = 2;
            }
            .build()


        // Simulate successful votes from all players
        val players = listOf("Player1", "Player2")
        players.forEach { player ->
            val vote = SingleVote(username = player, uservote = true)
            lobby.voteOnAdventure(vote)
        }

        // Verify the expected behavior and outcomes of the function
        // Assert that the adventure succeeds, scores are updated accordingly,
        // and the winner is not determined yet.
        assertEquals(SCORE.GOOD, lobby.info.scores[currentAdventure - 1])
        assertEquals(WINNER.NOT_DECIDED,lobby.info.winner)

        // Verify that the function progresses to the next round
        assertEquals(2, lobby.info.currentRound)
        assertEquals(false, lobby.info.isAdventure)
        assertEquals(emptyList<String>(), lobby.info.selectedForAdventure)
        assertNotEquals("Player1", lobby.info.king)
        assertEquals(3, lobby.info.playerSelectNum)
    }

    @Test
    fun `voteOnAdventure should update scores and progress to next round when all players vote(faliure) successfully`() {
        val currentAdventure = 1

        // Create a test instance of the Lobby class
        // Set up the necessary data for the test
        val lobby = LobbyBuilder("lobby_vote_on_adventure_test")
            .info{
                king = "Player1"
                currentRound= 1
                this.currentAdventure = currentAdventure
                this.isAdventure = true
                playerSelectNum = 2
                playersName = mutableListOf("Player1", "Player2","Player3", "Player4","Player5")

            }
            .addAdventureVote {
                king = "Player1"
                chosen = mutableListOf("Player1", "Player2")
                results = mutableListOf()
            }
            .apply {
                this.selectionRound = 2
            }
            .build()


        // Simulate successful votes from all players
        val players = listOf("Player1", "Player2")
        players.forEach { player ->
            val vote = SingleVote(username = player, uservote = false)
            lobby.voteOnAdventure(vote)
        }

        // Verify the expected behavior and outcomes of the function
        // Assert that the adventure succeeds, scores are updated accordingly,
        // and the winner is not determined yet.
        assertEquals(SCORE.EVIL, lobby.info.scores[currentAdventure - 1])
        assertEquals(WINNER.NOT_DECIDED,lobby.info.winner)

        // Verify that the function progresses to the next round
        assertEquals(2, lobby.info.currentRound)
        assertEquals(false, lobby.info.isAdventure)
        assertEquals(emptyList<String>(), lobby.info.selectedForAdventure)
        assertNotEquals("Player1", lobby.info.king)
        assertEquals(3, lobby.info.playerSelectNum)
    }

    @Test
    fun `voteOnAdventure should declare evil as the winner when evil scores reach the winning threshold`() {
        val currentAdventure = 4

        // Create a test instance of the Lobby class
        // Set up the necessary data for the test
        val lobby = LobbyBuilder("lobby_vote_on_adventure_test")
            .info {
                king = "Player1"
                isAdventure = true
                this.currentAdventure = currentAdventure
                playersName = mutableListOf("Player1", "Player2", "Player3", "Player4", "Player5")
                scores = mutableListOf(SCORE.EVIL, SCORE.EVIL, SCORE.GOOD, SCORE.UNDECIDED, SCORE.UNDECIDED)
            }
            //
            .addAdventureVote {  }
            .addAdventureVote {  }
            .addAdventureVote {  }
            .addAdventureVote {
                king = "Player1"
                chosen = mutableListOf("Player1", "Player2", "Player3")
                results = mutableListOf(SingleVote("Player1", true), SingleVote("Player2", false))
            }
            .build()

        // Invoke the voteOnAdventure function to trigger evil winning
        val vote = SingleVote(username = "Player3", uservote = false)
        lobby.voteOnAdventure(vote)

        // Verify the expected behavior and outcomes of the function
        // Assert that evil scores reach the winning threshold
        assertEquals(WINNER.EVIL, lobby.info.winner)
    }

    @Test
    fun `voteOnAdventure should declare good as the winner when good scores reach the winning threshold`() {
        val currentAdventure = 4

        // Create a test instance of the Lobby class
        // Set up the necessary data for the test
        val lobby = LobbyBuilder("lobby_vote_on_adventure_test")
            .info {
                king = "Player1"
                isAdventure = true
                this.currentAdventure = currentAdventure
                playersName = mutableListOf("Player1", "Player2", "Player3", "Player4", "Player5")
                scores = mutableListOf(SCORE.EVIL, SCORE.GOOD, SCORE.GOOD, SCORE.UNDECIDED, SCORE.UNDECIDED)
            }
            //
            .addAdventureVote {  }
            .addAdventureVote {  }
            .addAdventureVote {  }
            .addAdventureVote {
                king = "Player1"
                chosen = mutableListOf("Player1", "Player2", "Player3")
                results = mutableListOf(SingleVote("Player1", true), SingleVote("Player2", true))
            }
            .build()

        // Invoke the voteOnAdventure function to trigger evil winning
        val vote = SingleVote(username = "Player3", uservote = true)
        lobby.voteOnAdventure(vote)

        // Verify the expected behavior and outcomes of the function
        // Assert that evil scores reach the winning threshold
        assertEquals(WINNER.GOOD, lobby.info.winner)
    }






}
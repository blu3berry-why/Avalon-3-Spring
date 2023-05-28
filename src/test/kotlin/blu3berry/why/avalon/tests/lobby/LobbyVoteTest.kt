package blu3berry.why.avalon.tests.lobby

import blu3berry.why.avalon.converters.parseVotes
import blu3berry.why.avalon.mockdata.builders.LobbyBuilder
import blu3berry.why.avalon.model.db.lobby.extensions.vote
import blu3berry.why.avalon.model.enums.WINNER
import blu3berry.why.avalon.model.network.SingleVote
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class LobbyVoteTest {

    @Test
    fun `test vote updates lobby's vote`() {
        // Arrange
        val vote = SingleVote("player1", true)

        val lobby = LobbyBuilder("lobby_vote_1")
            .info {
                king = "king"
                currentRound = 1
                playersName = mutableListOf("player1", "player2", "player3", "player4", "player5")
            }
            .addVote {
                king = "king"
                chosen = mutableListOf("player2", "player3")
                results = mutableListOf()
            }
            .build()

        // Act
        lobby.vote(vote)

        // Assert
        assertEquals(vote, lobby.votes[lobby.info.currentRound].results.first())
    }

   /* @Test
    fun `test vote updates lobby's vote when user has already voted`() {
        // Arrange
        val vote = SingleVote("player1", true)
        val username = "player1"

        val lobby = LobbyBuilder("lobby_vote_2")
            .info {
                king = "king"
                currentRound = 1
                playersName = mutableListOf("player1", "player2", "player3", "player4", "player5")
            }
            .addVote {
                king = "king"
                chosen = mutableListOf("player2", "player3")
                results = mutableListOf(SingleVote("player1", false))
            }
            .build()

        // Act
        lobby.vote(vote)

        // Assert
        assertEquals(vote, lobby.votes[1].results[0])
    }*/



   /* @Test
    fun `test vote throws exception when user is not a valid player in the lobby`() {
        // Arrange
        val vote = SingleVote("player99", true) // User not present in the lobby
        val username = "player99"

        val lobby = LobbyBuilder("lobby_vote_3")
            .info {
                king = "king"
                currentRound = 1
                playersName = mutableListOf("player1", "player2", "player3", "player4", "player5")
            }
            .addVote {
                king = "king"
                chosen = mutableListOf("player2", "player3")
                results = mutableListOf(SingleVote("player1", false))
            }
            .build()

        // Act & Assert
        val exception = assertThrows(ConflictException::class.java) {
            lobby.vote(vote)
        }

        assertEquals("You are not a valid player in the lobby!", exception.message)
    }*/


    @Test
    fun `test vote updates lobby's results`() {
        // Arrange
        val username = "player5"
        val vote = SingleVote(username, false) // Player3 votes false

        val lobby = LobbyBuilder("lobby_vote_5")
            .info {
                king = username
                currentRound = 1
                playersName = mutableListOf("player1", "player2", "player3", "player4", "player5")
            }
            .addVote {
                king = username
                chosen = mutableListOf("player2", "player3")
                results = mutableListOf(
                    SingleVote("player1", false),
                    SingleVote("player2", false),
                    SingleVote("player3", false),
                    SingleVote("player4", false)
                )
            }
            .build()

        // Act
        lobby.vote(vote)

        // Assert
        val updatedVote = lobby.votes[1].results.firstOrNull { it.username == username }
        assertEquals(vote, updatedVote)
    }


    @ParameterizedTest
    @CsvSource(
        //not enough players, the votes and the player names do not matter
        "player1, true, *player2=false;player3=true*",
        "player2, false, player3=false;player4=true",
        "player3, true, player4=false;player5=true",
        "player4, false, player5=false;player1=true",

        //just not enough people
        "player1, true, player2=false;player3=true;player4=false",
        //player 2 already voted
        "player2, false, player2=false;player3=false;player4=true;player5=true",
        //no votes
        "player3, true, empty",
    )
    fun `test vote updates lobby's results and stays in the round`(
        username: String,
        voteValue: Boolean,
        votesList: String
    ) {
        // Arrange
        val vote = SingleVote(username, voteValue)
        val votes = parseVotes(votesList)

        val lobby = LobbyBuilder("lobby_vote_5")
            .info {
                king = username
                currentRound = 1
                playersName = mutableListOf("player1", "player2", "player3", "player4", "player5")
            }
            .addVote {
                king = username
                chosen = mutableListOf("player2", "player3")
                results = votes
            }
            .build()

        // Act
        lobby.vote(vote)

        // Assert
        assertEquals(1, lobby.info.currentRound)
        assertEquals(0, lobby.info.failCounter)
        assertEquals(WINNER.NOT_DECIDED, lobby.info.winner)
    }



    @ParameterizedTest
    @CsvSource(
        //needs the majority
        // Player voting true
        // 2 true : 3 false
        "player1, true, player2=true;player3=false;player4=false;player5=false",
        // 1 true : 4 false
        "player1, true, player2=false;player3=false;player4=false;player5=false",

        // Player voting false
        // 2 true : 3 false
        "player1, false, player2=true;player3=true;player4=false;player5=false",
        // 1 true : 4 false
        "player1, false, player2=true;player3=false;player4=false;player5=false",
        // 0 true : 5 false
        "player1, false, player2=false;player3=false;player4=false;player5=false",
    )
    fun `test vote updates lobby's results and advances to nextRound and fail counter increases`(
        username: String,
        voteValue: Boolean,
        votesList: String
    ) {
        // Arrange
        val vote = SingleVote(username, voteValue)
        val votes = parseVotes(votesList)

        val lobby = LobbyBuilder("lobby_vote_5")
            .info {
                king = username
                currentRound = 1
                playersName = mutableListOf("player1", "player2", "player3", "player4", "player5")
                failCounter = 0
            }
            .addVote {
                king = username
                chosen = mutableListOf("player2", "player3")
                results = votes
            }
            .build()

        // Act
        lobby.vote(vote)

        // Assert
        assertEquals(2, lobby.info.currentRound)
        assertEquals(1, lobby.info.failCounter)
        assertEquals(WINNER.NOT_DECIDED, lobby.info.winner)
        assertEquals(2, lobby.info.playerSelectNum)
    }


    @ParameterizedTest
    @CsvSource(
        //needs the majority
        // Player voting true
        // 3 true : 2 false
        "player1, true, player2=true;player3=true;player4=false;player5=false",
        // 4 true : 1 false
        "player1, true, player2=true;player3=true;player4=true;player5=false",
        // 5 true : 0 false
        "player1, true, player2=true;player3=true;player4=true;player5=true",

        // Player voting false
        // 3 true : 2 false
        "player1, false, player2=true;player3=true;player4=true;player5=false",
        // 4 true : 4 false
        "player1, false, player2=true;player3=true;player4=true;player5=true",
    )
    fun `test vote updates lobby's results and advances to adventure`(
        username: String,
        voteValue: Boolean,
        votesString: String
    ) {
        // Arrange
        val vote = SingleVote(username, voteValue)
        val votes = parseVotes(votesString)

        val lobby = LobbyBuilder("lobby_vote_5")
            .info {
                king = username
                currentRound = 1
                playersName = mutableListOf("player1", "player2", "player3", "player4", "player5")
            }
            .addVote {
                king = username
                chosen = mutableListOf("player2", "player3")
                results = votes
            }
            .build()

        // Act
        lobby.vote(vote)

        // Assert
        assertEquals(1, lobby.info.currentRound)
        assertEquals(1, lobby.info.currentAdventure)
        assertEquals(0, lobby.info.failCounter)
        assertEquals(WINNER.NOT_DECIDED, lobby.info.winner)
    }


    @Test
    fun `test reaching maximum fails sets winner to EVIL`() {
        // Arrange
        val vote = SingleVote("player1", false)

        val lobby = LobbyBuilder("lobby_max_fails")
            .info {
                king = "player1"
                currentRound = 1
                playersName = mutableListOf("player1", "player2", "player3", "player4", "player5")
                failCounter = 4
            }
            .addVote {
                king = "player1"
                chosen = mutableListOf("player1", "player2")
                results = mutableListOf(
                    SingleVote(username = "player2", uservote = false),
                    SingleVote(username = "player3", uservote = false),
                    SingleVote(username = "player4", uservote = false),
                    SingleVote(username = "player5", uservote = false),
                )
            }
            .build()

        // Act
        lobby.vote(vote)

        // Assert
        assertEquals(WINNER.EVIL, lobby.info.winner)
    }


    @Test
    fun `test vote tie in votes`() {
        // Arrange
        val vote1 = SingleVote("player1", true)
        val vote2 = SingleVote("player2", true)
        val vote3 = SingleVote("player3", true)
        val vote4 = SingleVote("player4", false)
        val vote5 = SingleVote("player5", false)

        val lobby = LobbyBuilder("lobby_vote_tie")
            .info {
                king = "player5"
                currentRound = 1
                playersName = mutableListOf("player1", "player2", "player3", "player4", "player5", "player6")
            }
            .addVote {
                king = "player1"
                chosen = mutableListOf("player1", "player2")
                results = mutableListOf(vote1, vote2, vote3, vote4, vote5)
            }
            .build()

        // Act
        lobby.vote(SingleVote("player6", false))

        // Assert
        assertEquals(2, lobby.info.currentRound)
        assertEquals(0, lobby.info.currentAdventure)
        assertEquals(1, lobby.info.failCounter)
        assertEquals(WINNER.NOT_DECIDED, lobby.info.winner)
        assertEquals(2, lobby.info.playerSelectNum)
    }


}
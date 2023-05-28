package blu3berry.why.avalon.tests.services;

import blu3berry.why.avalon.api.errorhandling.exceptions.ConflictException
import blu3berry.why.avalon.api.errorhandling.exceptions.ResourceNotFoundException
import blu3berry.why.avalon.converters.parseVotes
import blu3berry.why.avalon.dal.services.GameService
import blu3berry.why.avalon.dal.services.repository.LobbyRepository
import blu3berry.why.avalon.mockdata.MockLobbies
import blu3berry.why.avalon.mockdata.builders.LobbyBuilder
import blu3berry.why.avalon.model.db.UserRoleMap
import blu3berry.why.avalon.model.db.lobby.Lobby
import blu3berry.why.avalon.model.db.lobby.extensions.currentChosen
import blu3berry.why.avalon.model.enums.ROLE
import blu3berry.why.avalon.model.enums.WINNER
import blu3berry.why.avalon.model.network.Message
import blu3berry.why.avalon.model.network.SingleVote
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

class GameServiceTest {

    @Autowired
    private lateinit var gameService: GameService

    @Mock
    private lateinit var lobbyRepository: LobbyRepository

    private lateinit var testLobby:Lobby

    @BeforeEach
    fun setup(){
        MockitoAnnotations.openMocks(this)
        gameService = GameService(lobbyRepository)

        testLobby= MockLobbies.normalLobby()
        Mockito.`when`(lobbyRepository.findLobbyByLobbyCode("test_lobby_code")).thenReturn(testLobby)
    }

    @Test
    fun `test two lobby equals (true)`() {
        val normalLobby = MockLobbies.normalLobby()
        assertEquals(true, MockLobbies.isLobbyEqual(normalLobby, testLobby))
    }

    @Test
    fun `test two lobby equals (false)`() {
        val normalLobby = MockLobbies.normalLobby().apply {
            this.info.playersName.add("New player name which is not in the other lobby")
        }
        assertEquals(false, MockLobbies.isLobbyEqual(normalLobby, testLobby))
    }

    @Test
    fun `test get game's info`(){

        //needs to be lowercase because the findLobbyByCode gets it lowercased
        val info = gameService.getGameInfo("test_lobby_code")

        assertEquals(true, MockLobbies.isInfoEqual(testLobby.info, info))
    }

    @Test
    fun `lobby with code not exists`(){
        val lobbyCode = "nonExistentLobbyCode"

        val exception = assertThrows(ResourceNotFoundException::class.java) {
            gameService.getGameInfo(lobbyCode)
        }

        assertEquals("Lobby with this code does not exists.", exception.message)
    }


    @Test
    fun `get character sees nothing`(){
        testLobby.userRoles.add(3, UserRoleMap("username", ROLE.ARNOLD))

        val role = gameService.getCharacter("test_lobby_code", "username")

        assertEquals(ROLE.ARNOLD, role.name)
        assertEquals(listOf<String>(), role.sees)
    }

    @Test
    fun `get character(evil) sees evil minions of morderd`(){
        testLobby.userRoles = mutableListOf<UserRoleMap>(
            UserRoleMap("user1", ROLE.MINION_OF_MORDRED),
            UserRoleMap("user2", ROLE.MINION_OF_MORDRED),
            UserRoleMap("username", ROLE.MINION_OF_MORDRED)
        )

        val role = gameService.getCharacter("test_lobby_code", "username")

        assertEquals(ROLE.MINION_OF_MORDRED, role.name)
        assertEquals(listOf<String>("user1", "user2"), role.sees)
    }

    @Test
    fun `get character(evil) sees evil others (Assassin, Moreded, Morgana)`(){
        testLobby.userRoles = mutableListOf<UserRoleMap>(
            UserRoleMap("user1", ROLE.ASSASSIN),
            UserRoleMap("user2", ROLE.MORDRED),
            UserRoleMap("user3", ROLE.MORGANA),
            UserRoleMap("username", ROLE.MINION_OF_MORDRED)
        )

        val role = gameService.getCharacter("test_lobby_code", "username")

        assertEquals(ROLE.MINION_OF_MORDRED, role.name)
        assertEquals(listOf<String>("user1", "user2", "user3"), role.sees)
    }

    @Test
    fun `get character(evil) doesn't see Oberon`(){
        testLobby.userRoles = mutableListOf<UserRoleMap>(
            UserRoleMap("user1", ROLE.OBERON),
            UserRoleMap("username", ROLE.MINION_OF_MORDRED)
        )

        val role = gameService.getCharacter("test_lobby_code", "username")

        assertEquals(ROLE.MINION_OF_MORDRED, role.name)
        assertEquals(listOf<String>(), role.sees)
    }

    @Test
    fun `get character good doesn't se anything`(){
        testLobby.userRoles = mutableListOf<UserRoleMap>(
            UserRoleMap("user1", ROLE.ASSASSIN),
            UserRoleMap("user2", ROLE.MORDRED),
            UserRoleMap("user3", ROLE.MORGANA),
            UserRoleMap("user4", ROLE.OBERON),
            UserRoleMap("user5", ROLE.PERCIVAL),
            UserRoleMap("user6", ROLE.MERLIN),
            UserRoleMap("user7", ROLE.MINION_OF_MORDRED),
            UserRoleMap("user8", ROLE.ARNOLD),
            UserRoleMap("user9", ROLE.SERVANT_OF_ARTHUR),
            UserRoleMap("username", ROLE.SERVANT_OF_ARTHUR)
        )

        val role = gameService.getCharacter("test_lobby_code", "username")

        assertEquals(ROLE.SERVANT_OF_ARTHUR, role.name)
        assertEquals(listOf<String>(), role.sees)
    }

    @Test
    fun `get character(Merlin) sees Assassin, Morgana, Oberon, Minion of Mordred but not Mordred`(){
        testLobby.userRoles = mutableListOf<UserRoleMap>(
            UserRoleMap("user1", ROLE.ASSASSIN),
            UserRoleMap("user2", ROLE.MORDRED),
            UserRoleMap("user3", ROLE.MORGANA),
            UserRoleMap("user4", ROLE.OBERON),
            UserRoleMap("user5", ROLE.PERCIVAL),
            UserRoleMap("user7", ROLE.MINION_OF_MORDRED),
            UserRoleMap("user8", ROLE.ARNOLD),
            UserRoleMap("user9", ROLE.SERVANT_OF_ARTHUR),
            UserRoleMap("username", ROLE.MERLIN)
        )

        val role = gameService.getCharacter("test_lobby_code", "username")

        assertEquals(ROLE.MERLIN, role.name)
        assertEquals(listOf<String>("user1", "user3", "user4", "user7"), role.sees)
    }

    @Test
    fun `get character(Percival) sees Morgana and Merlin`(){
        testLobby.userRoles = mutableListOf<UserRoleMap>(
            UserRoleMap("user1", ROLE.ASSASSIN),
            UserRoleMap("user2", ROLE.MORDRED),
            UserRoleMap("user3", ROLE.MORGANA),
            UserRoleMap("user4", ROLE.OBERON),
            UserRoleMap("user6", ROLE.MERLIN),
            UserRoleMap("user7", ROLE.MINION_OF_MORDRED),
            UserRoleMap("user8", ROLE.ARNOLD),
            UserRoleMap("user9", ROLE.SERVANT_OF_ARTHUR),
            UserRoleMap("username", ROLE.PERCIVAL)
        )

        val role = gameService.getCharacter("test_lobby_code", "username")

        assertEquals(ROLE.PERCIVAL, role.name)
        assertEquals(listOf<String>("user3", "user6"), role.sees)
    }


    @Test
    fun `test select throws exception when number of chosen players is incorrect`() {

        // should be 2 this is why it fails
        val chosen = listOf("player1", "player2", "player3")
        val username = "user1"
        val kingName = "user1"

        val lobby = LobbyBuilder("lobby_select_1")
            .info {
                king = kingName
                currentRound = 1
                playersName = mutableListOf("player1", "player2", "player3", "player4", "player5")
            }
            .build()

        // Mock the LobbyRepository
        `when`(lobbyRepository.findLobbyByLobbyCode("lobby_select_1")).thenReturn(lobby)


        val exception = assertThrows(ConflictException::class.java) {
            gameService.select("lobby_select_1", chosen, username) // Pass the lobby code to the select function
        }

        assertEquals("This is not the required amount of people! Required: 2, but found : 3!", exception.message)
    }



    @Test
    fun `test select throws exception when king has already chosen`() {
        val chosen = listOf("player1", "player2")
        val username = "user1"
        val kingName = "user1"

        val lobby = LobbyBuilder("lobby_select_2")
            .apply {
                info {
                    king = kingName
                    currentRound = 1
                    playersName = mutableListOf("player1", "player2", "player3", "player4", "player5")
                }
                addVote {
                    king = kingName
                    this.chosen = mutableListOf("player3", "player1")
                    this.results = mutableListOf()
                }

            }
            .build()

        `when`(lobbyRepository.findLobbyByLobbyCode("lobby_select_2")).thenReturn(lobby)

        val exception = assertThrows(ConflictException::class.java) {
            gameService.select("lobby_select_2", chosen, username)
        }

        assertEquals("The king has already chosen", exception.message)
    }


    @Test
    fun `test select updates lobby's chosen players`() {
        val chosenNames = listOf("player1", "player2")
        val username = "user1"
        val kingName = "user1"

        val lobby = LobbyBuilder("lobby_select_3")
            .info {
                king = kingName
                currentRound = 1
                playersName = mutableListOf("player1", "player2", "player3", "player4", "player5")
            }
            // The first rounds votes
            .addVote {
                king = kingName
                chosen = mutableListOf()
                results = mutableListOf()
            }
            .build()

        `when`(lobbyRepository.findLobbyByLobbyCode("lobby_select_3")).thenReturn(lobby)

        val message = gameService.select("lobby_select_3", chosenNames, username)

        // Verify that the lobby's chosen players have been updated
        val updatedLobby = lobbyRepository.findLobbyByLobbyCode("lobby_select_3")
        assertEquals(chosenNames, updatedLobby?.currentChosen)

        assertEquals(Message.OK, message)
    }


    @Test
    fun `test select throws exception when user is not the king`() {
        val chosen = listOf("player1", "player2")
        val username = "user1"
        val kingName = "user2" // User2 is the king, not user1

        val lobby = LobbyBuilder("lobby_select_4")
            .info {
                king = kingName
            }
            .build()

        `when`(lobbyRepository.findLobbyByLobbyCode("lobby_select_4")).thenReturn(lobby)

        val exception = assertThrows(ConflictException::class.java) {
            gameService.select("lobby_select_4", chosen, username)
        }
        assertEquals("You are not the king!", exception.message)
    }



    @Test
    fun `test select throws exception when chosen players list is empty`() {
        val chosen: List<String> = emptyList()
        val username = "user1"
        val kingName = "user1"

        val lobby = LobbyBuilder("lobby_select_1")
            .info {
                king = kingName
                currentRound = 1
                playersName = mutableListOf("player1", "player2", "player3", "player4", "player5")
            }
            .build()

        // Mock the LobbyRepository
        `when`(lobbyRepository.findLobbyByLobbyCode("lobby_select_1")).thenReturn(lobby)

        val exception = assertThrows(ConflictException::class.java) {
            gameService.select("lobby_select_1", chosen, username)
        }

        assertEquals("This is not the required amount of people! Required: 2, but found : 0!", exception.message)
    }


    @Test
    fun `test select throws exception when chosen players list contains duplicates`() {
        val chosen = listOf("player1", "player1") // Contains duplicate entry "player1"
        val username = "user1"
        val kingName = "user1"

        val lobby = LobbyBuilder("lobby_select_1")
            .info {
                king = kingName
                currentRound = 1
                playersName = mutableListOf("player1", "player2", "player3", "player4", "player5")
            }
            .build()

        // Mock the LobbyRepository
        `when`(lobbyRepository.findLobbyByLobbyCode("lobby_select_1")).thenReturn(lobby)

        val exception = assertThrows(ConflictException::class.java) {
            gameService.select("lobby_select_1", chosen, username)
        }

        assertEquals("Repeated usernames!", exception.message)
    }


    @Test
    fun `test select throws exception when chosen players list exceeds maximum limit`() {
        val chosen = listOf("player1", "player2", "player3", "player4", "player5", "player6") // Exceeds maximum limit of 5 players
        val username = "user1"
        val kingName = "user1"

        val lobby = LobbyBuilder("lobby_select_1")
            .info {
                king = kingName
                currentRound = 1
                playersName = mutableListOf("player1", "player2", "player3", "player4", "player5")
            }
            .build()

        // Mock the LobbyRepository
        `when`(lobbyRepository.findLobbyByLobbyCode("lobby_select_1")).thenReturn(lobby)

        val exception = assertThrows(ConflictException::class.java) {
            gameService.select("lobby_select_1", chosen, username)
        }

        assertEquals("This is not the required amount of people! Required: 2, but found : 6!", exception.message)
    }


    @Test
    fun `test select throws exception when user is not a valid player in the lobby`() {
        val chosen = listOf("player1", "player2") // Valid chosen players list
        val username = "user99" // User not present in the lobby
        val kingName = "user1"

        val lobby = LobbyBuilder("lobby_select_1")
            .info {
                king = kingName
                currentRound = 1
                playersName = mutableListOf("player1", "player2", "player3", "player4", "player5")
            }
            .build()

        // Mock the LobbyRepository
        `when`(lobbyRepository.findLobbyByLobbyCode("lobby_select_1")).thenReturn(lobby)

        val exception = assertThrows(ConflictException::class.java) {
            gameService.select("lobby_select_1", chosen, username)
        }

        assertEquals("You are not the king!", exception.message)
    }

    @Test
    fun `test vote updates lobby's vote`() {
        val vote = SingleVote("player1", true)
        val username = "player1"

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

        `when`(lobbyRepository.findLobbyByLobbyCode("lobby_vote_1")).thenReturn(lobby)

        val message = gameService.vote("lobby_vote_1", vote)

        val updatedLobby = lobbyRepository.findLobbyByLobbyCode("lobby_vote_1")

        assertEquals(vote, updatedLobby?.votes?.get(1)?.results?.get(0))
        assertEquals(Message.OK, message)
    }

    @Test
    fun `test vote updates lobby's vote when user has already voted`() {
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

        `when`(lobbyRepository.findLobbyByLobbyCode("lobby_vote_2")).thenReturn(lobby)

        val message = gameService.vote("lobby_vote_2", vote)

        val updatedLobby = lobbyRepository.findLobbyByLobbyCode("lobby_vote_2")

        assertEquals(vote, updatedLobby?.votes?.get(1)?.results?.get(0))
        assertEquals(Message.OK, message)
    }


    @Test
    fun `test vote throws exception when user is not a valid player in the lobby`() {
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

        `when`(lobbyRepository.findLobbyByLobbyCode("lobby_vote_3")).thenReturn(lobby)

        val exception = assertThrows(ConflictException::class.java) {
            gameService.vote("lobby_vote_3", vote)
        }

        assertEquals("You are not a valid player in the lobby!", exception.message)
    }

    @Test
    fun `test vote updates lobby's results`() {
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

        `when`(lobbyRepository.findLobbyByLobbyCode("lobby_vote_5")).thenReturn(lobby)

        val message = gameService.vote("lobby_vote_5", vote)

        val updatedLobby = lobbyRepository.findLobbyByLobbyCode("lobby_vote_5")

        assertEquals(vote, updatedLobby?.votes?.get(1)?.results?.firstOrNull { it.username == username })
        assertEquals(Message.OK, message)
    }

    @ParameterizedTest
    @CsvSource(
            //not enough players, the votes and the player names does not matter
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

        `when`(lobbyRepository.findLobbyByLobbyCode("lobby_vote_5")).thenReturn(lobby)

        val message = gameService.vote("lobby_vote_5", vote)

        val updatedLobby = lobbyRepository.findLobbyByLobbyCode("lobby_vote_5")

        assertEquals(Message.OK, message)
        assertEquals(1, updatedLobby?.info?.currentRound)
        assertEquals(0, updatedLobby?.info?.failCounter)
        assertEquals(WINNER.NOT_DECIDED, updatedLobby?.info?.winner)


    }


    @ParameterizedTest
    @CsvSource(
        //needs the majority
        // Player voting true
        //2 true : 3 false
        "player1, true, player2=true;player3=false;player4=false;player5=false",
        //1 true : 4 false
        "player1, true, player2=false;player3=false;player4=false;player5=false",


        // Player voting false
        //2 true : 3 false
        "player1, false, player2=true;player3=true;player4=false;player5=false",
        //1 true : 4 false
        "player1, false, player2=true;player3=false;player4=false;player5=false",
        //0 true : 5 false
        "player1, false, player2=false;player3=false;player4=false;player5=false",
    )

    fun `test vote updates lobby's results and advences to nextRound and fail counter increases`(
        username: String,
        voteValue: Boolean,
        votesList: String
    ) {
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

        `when`(lobbyRepository.findLobbyByLobbyCode("lobby_vote_5")).thenReturn(lobby)

        val message = gameService.vote("lobby_vote_5", vote)

        val updatedLobby = lobbyRepository.findLobbyByLobbyCode("lobby_vote_5")

        assertEquals(Message.OK, message)
        assertEquals(2, updatedLobby?.info?.currentRound)
        assertEquals(1, updatedLobby?.info?.failCounter)
        assertEquals(WINNER.NOT_DECIDED, updatedLobby?.info?.winner)
        assertEquals(2, updatedLobby?.info?.playerSelectNum)


    }

    @ParameterizedTest
    @CsvSource(
        //needs the majority
        // Player voting true
        //3 true : 2 false
        "player1, true, player2=true;player3=true;player4=false;player5=false",
        //4 true : 1 false
        "player1, true, player2=true;player3=true;player4=true;player5=false",
        //5 true : 0 false
        "player1, true, player2=true;player3=true;player4=true;player5=true",

        // Player voting false
        //3 true : 2 false
        "player1, false, player2=true;player3=true;player4=true;player5=false",
        //4 true : 4 false
        "player1, false, player2=true;player3=true;player4=true;player5=true",

    )
    fun `test vote updates lobby's results and advances to adventure`(
        username: String,
        voteValue: Boolean,
        votesString: String
    ) {
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

        `when`(lobbyRepository.findLobbyByLobbyCode("lobby_vote_5")).thenReturn(lobby)

        val message = gameService.vote("lobby_vote_5", vote)

        val updatedLobby = lobbyRepository.findLobbyByLobbyCode("lobby_vote_5")

        assertEquals(Message.OK, message)
        assertEquals(1, updatedLobby?.info?.currentRound)
        assertEquals(1, updatedLobby?.info?.currentAdventure)
        assertEquals(0, updatedLobby?.info?.failCounter)
        assertEquals(WINNER.NOT_DECIDED, updatedLobby?.info?.winner)

    }

    @Test
    fun `test reaching maximum fails sets winner to EVIL`() {
        val vote = SingleVote("player1", false);

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

        `when`(lobbyRepository.findLobbyByLobbyCode("lobby_vote_5")).thenReturn(lobby)

         gameService.vote("lobby_vote_5", vote)

        val updatedLobby = lobbyRepository.findLobbyByLobbyCode("lobby_vote_5")

        assertEquals(WINNER.EVIL, updatedLobby?.info?.winner)
    }

    @Test
    fun `test vote tie in votes`() {
        val vote1 = SingleVote("player1", true)
        val vote2 = SingleVote("player2", true)
        val vote3 = SingleVote("player3", true)
        val vote4 = SingleVote("player4", false)
        val vote5 = SingleVote("player5", false)


        val lobby = LobbyBuilder("lobby_vote_tie")
            .info {
                king = "player5"
                currentRound = 1
                playersName = mutableListOf("player1", "player2", "player3", "player4","player5","player6")
            }
            .addVote {
                king = "player1"
                chosen = mutableListOf("player1", "player2")
                results = mutableListOf(vote1, vote2, vote3, vote4, vote5)
            }
            .build()

        `when`(lobbyRepository.findLobbyByLobbyCode("lobby_vote_tie")).thenReturn(lobby)

        val message = gameService.vote("lobby_vote_tie", SingleVote("player6", false))

        val updatedLobby = lobbyRepository.findLobbyByLobbyCode("lobby_vote_tie")

        // Assert that the adventure does not start due to a tie in votes
        assertEquals(Message.OK, message)
        assertEquals(2, updatedLobby?.info?.currentRound)
        assertEquals(0, updatedLobby?.info?.currentAdventure)
        assertEquals(1, updatedLobby?.info?.failCounter)
        assertEquals(WINNER.NOT_DECIDED, updatedLobby?.info?.winner)
        assertEquals(2, updatedLobby?.info?.playerSelectNum)
    }


















}
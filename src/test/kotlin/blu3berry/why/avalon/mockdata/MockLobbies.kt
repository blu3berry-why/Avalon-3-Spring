package blu3berry.why.avalon.mockdata

import blu3berry.why.avalon.model.db.UserRoleMap
import blu3berry.why.avalon.model.db.lobby.Lobby
import blu3berry.why.avalon.model.enums.ROLE
import blu3berry.why.avalon.model.enums.SCORE
import blu3berry.why.avalon.model.enums.WINNER
import blu3berry.why.avalon.model.network.Info
import blu3berry.why.avalon.model.network.RoundVote
import blu3berry.why.avalon.model.network.Settings
import blu3berry.why.avalon.model.network.SingleVote

class MockLobbies {

    companion object {
        fun normalLobby() = Lobby(
            _id = null,
            lobbyCode = "testLobbyCode",
            info = Info(
                started = true,
                winner = WINNER.NOT_DECIDED,
                scores = mutableListOf(SCORE.GOOD, SCORE.GOOD, SCORE.EVIL,SCORE.UNDECIDED,SCORE.UNDECIDED),
                currentRound = 3,
                isAdventure = true,
                currentAdventure = 2,
                king = "John",
                failCounter = 1,
                selectedForAdventure = mutableListOf("Alice", "Bob", "Charlie"),
                playersName = mutableListOf("Alice", "Bob", "Charlie", "Dave"),
                assassinHasGuessed = false,
                playerSelectNum = 2
            ),
            settings = Settings(
                assassin = true,
                mordred = true,
                morgana = false,
                oberon = false,
                percival = true,
                arnold = false
            ),
            votes = mutableListOf(
                RoundVote(
                    king = "John",
                    chosen = mutableListOf("Alice", "Bob"),
                    results = mutableListOf(
                        SingleVote("Alice", true),
                        SingleVote("Bob", false),
                        SingleVote("Charlie", true)
                    )
                )
            ),
            adventureVotes = mutableListOf(
                RoundVote(
                    king = "John",
                    chosen = mutableListOf("Alice", "Bob"),
                    results = mutableListOf(
                        SingleVote("Alice", true),
                        SingleVote("Bob", false),
                        SingleVote("Charlie", true)
                    )
                )
            ),
            userRoles = mutableListOf(
                UserRoleMap("Alice", ROLE.PERCIVAL),
                UserRoleMap("Bob", ROLE.ASSASSIN),
                UserRoleMap("Charlie", ROLE.MERLIN)
            )
        )



    fun isLobbyEqual(lobby1: Lobby, lobby2: Lobby): Boolean {
        if (lobby1._id != lobby2._id) return false
        if (lobby1.lobbyCode != lobby2.lobbyCode) return false
        if (!isInfoEqual(lobby1.info, lobby2.info)) return false
        if (!isSettingsEqual(lobby1.settings, lobby2.settings)) return false
        if (!isVotesEqual(lobby1.votes, lobby2.votes)) return false
        if (!isVotesEqual(lobby1.adventureVotes, lobby2.adventureVotes)) return false
        if (!isUserRolesEqual(lobby1.userRoles, lobby2.userRoles)) return false
        return true
    }

    fun isInfoEqual(info1: Info, info2: Info): Boolean {
        if (info1.started != info2.started) return false
        if (info1.winner != info2.winner) return false
        if (info1.scores != info2.scores) return false
        if (info1.currentRound != info2.currentRound) return false
        if (info1.isAdventure != info2.isAdventure) return false
        if (info1.currentAdventure != info2.currentAdventure) return false
        if (info1.king != info2.king) return false
        if (info1.failCounter != info2.failCounter) return false
        if (info1.selectedForAdventure != info2.selectedForAdventure) return false
        if (info1.playersName != info2.playersName) return false
        if (info1.assassinHasGuessed != info2.assassinHasGuessed) return false
        if (info1.playerSelectNum != info2.playerSelectNum) return false
        return true
    }

    fun isSettingsEqual(settings1: Settings, settings2: Settings): Boolean {
        if (settings1.assassin != settings2.assassin) return false
        if (settings1.mordred != settings2.mordred) return false
        if (settings1.morgana != settings2.morgana) return false
        if (settings1.oberon != settings2.oberon) return false
        if (settings1.percival != settings2.percival) return false
        if (settings1.arnold != settings2.arnold) return false
        return true
    }

    fun isVotesEqual(votes1: List<RoundVote>, votes2: List<RoundVote>): Boolean {
        if (votes1.size != votes2.size) return false
        for (i in votes1.indices) {
            val vote1 = votes1[i]
            val vote2 = votes2[i]
            if (vote1.king != vote2.king) return false
            if (vote1.chosen != vote2.chosen) return false
            if (!isSingleVotesEqual(vote1.results, vote2.results)) return false
        }
        return true
    }

    fun isSingleVotesEqual(votes1: List<SingleVote>, votes2: List<SingleVote>): Boolean {
        if (votes1.size != votes2.size) return false
        for (i in votes1.indices) {
            val vote1 = votes1[i]
            val vote2 = votes2[i]
            if (vote1.username != vote2.username) return false
            if (vote1.uservote != vote2.uservote) return false
        }
        return true
    }

    fun isUserRolesEqual(userRoles1: List<UserRoleMap>, userRoles2: List<UserRoleMap>): Boolean {
        if (userRoles1.size != userRoles2.size) return false
        for (i in userRoles1.indices) {
            val userRole1 = userRoles1[i]
            val userRole2 = userRoles2[i]
            if (userRole1.userName != userRole2.userName) return false
            if (userRole1.role != userRole2.role) return false
        }
        return true
    }
}}
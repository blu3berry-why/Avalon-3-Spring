package blu3berry.why.avalon.mockdata.builders

import blu3berry.why.avalon.model.db.UserRoleMap
import blu3berry.why.avalon.model.db.lobby.Lobby
import blu3berry.why.avalon.model.network.Info
import blu3berry.why.avalon.model.network.RoundVote
import blu3berry.why.avalon.model.network.Settings

class LobbyBuilder(private val lobbyCode: String) {
    var info: Info? = null
    var settings: Settings? = null
    var votes: MutableList<RoundVote> = mutableListOf(RoundVote("", mutableListOf(), mutableListOf()))
    var adventureVotes: MutableList<RoundVote> = mutableListOf(RoundVote("", mutableListOf(), mutableListOf()))
    var userRoles: MutableList<UserRoleMap> = mutableListOf()
    var selectionRound: Int = 1

    fun info(block: InfoBuilder.() -> Unit): LobbyBuilder {
        val infoBuilder = InfoBuilder().apply(block)
        info = infoBuilder.build()
        return this
    }


    fun settings(block: SettingsBuilder.() -> Unit) : LobbyBuilder{
        val settingsBuilder = SettingsBuilder().apply(block)
        settings = settingsBuilder.build()
        return this
    }

    fun addVote(block: RoundVoteBuilder.() -> Unit) : LobbyBuilder{
        val voteBuilder = RoundVoteBuilder().apply(block)
        votes.add(voteBuilder.build())
        return this
    }

    fun addAdventureVote(block: RoundVoteBuilder.() -> Unit) : LobbyBuilder{
        val voteBuilder = RoundVoteBuilder().apply(block)
        adventureVotes.add(voteBuilder.build())
        return this
    }

    fun userRole(block: UserRoleMapBuilder.() -> Unit) : LobbyBuilder{
        val userRoleBuilder = UserRoleMapBuilder().apply(block)
        userRoles.add(userRoleBuilder.build())
        return this
    }

    fun build(): Lobby {
        return Lobby(
            null, lobbyCode,
            info ?: InfoBuilder().build(),
            settings ?: SettingsBuilder().build(),
            votes,
            adventureVotes,
            userRoles,
            selectionRound
        )
    }
}


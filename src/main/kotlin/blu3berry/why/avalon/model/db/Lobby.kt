package blu3berry.why.avalon.model.db

import blu3berry.why.avalon.model.network.Info
import blu3berry.why.avalon.model.network.Settings
import blu3berry.why.avalon.model.network.RoundVote
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "lobby")
data class Lobby(
    val lobbyCode: String,
    val info: Info,
    var settings: Settings,
    val votes: MutableList<RoundVote>,
    val adventureVotes: MutableList<RoundVote>,
    var userRoles: MutableList<UserRoleMap>
)

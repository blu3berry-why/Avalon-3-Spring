package blu3berry.why.avalon.model.db.lobby

import blu3berry.why.avalon.model.db.UserRoleMap
import blu3berry.why.avalon.model.network.Info
import blu3berry.why.avalon.model.network.RoundVote
import blu3berry.why.avalon.model.network.Settings
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId

@Document(collection = "lobby")
data class Lobby(
    @MongoId
    val _id: ObjectId?,
    val lobbyCode: String,
    val info: Info,
    var settings: Settings,
    val votes: MutableList<RoundVote>,
    val adventureVotes: MutableList<RoundVote>,
    var userRoles: MutableList<UserRoleMap>,
    var selectionRound: Int = 1
)

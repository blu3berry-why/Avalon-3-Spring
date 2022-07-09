package blu3berry.why.avalon.model.db.lobby.extensions

import blu3berry.why.avalon.model.db.lobby.Lobby
import blu3berry.why.avalon.model.enums.ROLE
import blu3berry.why.avalon.model.network.SingleVote


fun Lobby.voteOnAdventure(vote: SingleVote) = this.voteOnAdventure_impl(vote)

fun Lobby.vote(vote: SingleVote) = this.vote_impl(vote)

fun Lobby.sees(role: ROLE, username:String) = this.sees_impl(role, username)

fun Lobby.select(chosen:List<String>) = this.select_impl(chosen)

fun Lobby.start() = this.start_impl()

//NOTE: could be private at this time, but later we probably will use it as a public function
fun Lobby.setKing(playerName: String) {
    this.info.king = playerName
}
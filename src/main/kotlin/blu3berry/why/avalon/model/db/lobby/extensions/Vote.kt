package blu3berry.why.avalon.model.db.lobby.extensions

import blu3berry.why.avalon.model.db.lobby.Lobby
import blu3berry.why.avalon.model.enums.WINNER
import blu3berry.why.avalon.model.network.SingleVote

private val Lobby.currentResults: MutableList<SingleVote>
    get() = this.votes[this.info.currentRound].results

private fun Lobby.addVoteIfNotAlreadyVoted(vote: SingleVote) {
    currentResults.firstOrNull {
        it.username == vote.username
    } ?: currentResults.add(vote)
}

private fun Lobby.failedToSelectForAdventure() {
    this.info.failCounter++
    val maxFailsAllowed = 5
    if (this.info.failCounter == maxFailsAllowed) {
        this.info.winner = WINNER.EVIL
    }
}

internal fun Lobby.vote_impl(vote: SingleVote) {
    addVoteIfNotAlreadyVoted(vote)

    val everybodyVoted = currentResults.size == this.playerSize

    if (!everybodyVoted)
        return

    val approvingVotes = currentResults.filter { it.uservote }.size

    if (approvingVotes > (this.playerSize / 2)) {
        this.startAdventure()
    } else {
        failedToSelectForAdventure()
    }
    this.nextRound()
}
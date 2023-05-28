package blu3berry.why.avalon.model.db.lobby.extensions

import blu3berry.why.avalon.model.db.lobby.Lobby
import blu3berry.why.avalon.model.enums.WINNER
import blu3berry.why.avalon.model.network.RoundVote

internal val Lobby.playerSize: Int
    get() = this.info.playersName.size


internal val Lobby.currentChosen: MutableList<String>
    get() = this.votes[this.info.currentRound].chosen

internal fun Lobby.addNewVoteRound()  =
    this.votes.add(RoundVote(this.info.king!!, mutableListOf(), mutableListOf()))

internal fun Lobby.addNewAdventureVoteRound()  =
    this.adventureVotes.add(RoundVote(this.info.king!!, currentChosen, mutableListOf()))

private fun Lobby.findKingIndex(): Int {
    return this.info.playersName.indexOf(this.info.playersName.first {
        this.info.king == it
    })
}

private fun Lobby.nextKing(): String {
    var idx = findKingIndex() + 1

    if (idx == this.playerSize)
        idx = 0

    this.info.king = this.info.playersName[idx]

    return this.info.king!!
}

internal fun Lobby.nextRound() {
    this.info.currentRound++
    this.info.isAdventure = false
    this.info.selectedForAdventure.clear()

    this.setPlayerSelectNum()
    nextKing()
    addNewVoteRound()
}

internal fun Lobby.startAdventure() {
    this.info.failCounter = 0
    this.info.isAdventure = true
    this.info.currentAdventure++
    this.info.selectedForAdventure = currentChosen
    addNewAdventureVoteRound()
}

private val Lobby.hasWinner: Boolean
    get() = this.info.winner != WINNER.NOT_DECIDED
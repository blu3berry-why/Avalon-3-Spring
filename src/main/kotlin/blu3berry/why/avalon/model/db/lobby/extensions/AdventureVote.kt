package blu3berry.why.avalon.model.db.lobby.extensions

import blu3berry.why.avalon.api.errorhandling.exceptions.ConflictException
import blu3berry.why.avalon.model.db.lobby.Lobby
import blu3berry.why.avalon.model.enums.SCORE
import blu3berry.why.avalon.model.enums.WINNER
import blu3berry.why.avalon.model.helpers.Constants
import blu3berry.why.avalon.model.network.RoundVote
import blu3berry.why.avalon.model.network.SingleVote



private val Lobby.currentAdventureVotes: RoundVote
    get() = this.adventureVotes[this.info.currentAdventure]

private fun Lobby.checkIfChosen(vote: SingleVote) {
    currentAdventureVotes.chosen.firstOrNull { vote.username == it }
        ?: ConflictException.Throw("You are not chosen")
}

private fun Lobby.checkIfAlreadyVotedOnAdventure(vote: SingleVote) {
    if (currentAdventureVotes.results
            .firstOrNull { vote.username == it.username }
        != null
    )
        ConflictException.Throw("You already voted")
}

//TODO check if works!!!!
private fun Lobby.addEvilScore() {
    this.info.scores[(this.info.currentAdventure - 1)] = SCORE.EVIL
}

private fun Lobby.addGoodScore() {
    this.info.scores[(this.info.currentAdventure - 1)] = SCORE.GOOD
}

private fun Lobby.checkWinEvil() {
    if (this.info.scores.filter { it == SCORE.EVIL }.size >= 3)
        this.info.winner = WINNER.EVIL
}

private fun Lobby.checkWinGood() {
    if (this.info.scores.filter { it == SCORE.GOOD }.size >= 3)
        this.info.winner = WINNER.GOOD
}

private fun Lobby.checkWin() {
    checkWinEvil()
    checkWinGood()
}

internal fun Lobby.voteOnAdventure_impl(vote: SingleVote) {
    if (info.isAdventure.not())
        ConflictException.Throw("There is no adventure going on")

    checkIfChosen(vote)

    checkIfAlreadyVotedOnAdventure(vote)


    currentAdventureVotes.results.add(vote)

    var failLimit = 1

    if (this.info.currentAdventure == 4) {
        failLimit = Constants.adventureLimit[this.playerSize].failsRequiredOnFourth
    }

    val everybodyVoted = currentAdventureVotes.results.size == currentAdventureVotes.chosen.size

    if (everybodyVoted) {

        val evilVoteCount = currentAdventureVotes.results.filter { !it.uservote }.size
        val adventureFailed = evilVoteCount >= failLimit

        if (adventureFailed) {
            addEvilScore()
        } else {
            addGoodScore()
        }

        checkWin()

        nextRound()
    }
}
package blu3berry.why.avalon.dal.extensions

import blu3berry.why.avalon.api.errorhandling.exceptions.ConflictException
import blu3berry.why.avalon.model.helpers.Constants
import blu3berry.why.avalon.model.db.Lobby
import blu3berry.why.avalon.model.db.UserRoleMap
import blu3berry.why.avalon.model.enums.ROLE
import blu3berry.why.avalon.model.enums.SCORE
import blu3berry.why.avalon.model.enums.WINNER
import blu3berry.why.avalon.model.network.RoundVote
import blu3berry.why.avalon.model.network.SingleVote

data class SettingToRoleMapper(val checked: Boolean, val type: ROLE)

private fun Lobby.addEvil(roles: MutableList<ROLE>) {
    val numOfEvil = Constants.playerBalance[this.playerSize].evil

    fun needMoreEvil() = roles.size < numOfEvil
    val needMoreEvil = needMoreEvil()

    val evilRoles = listOf(
        SettingToRoleMapper(this.settings.assassin, ROLE.ASSASSIN),
        SettingToRoleMapper(this.settings.morgana, ROLE.MORGANA),
        SettingToRoleMapper(this.settings.mordred, ROLE.MORDRED),
        SettingToRoleMapper(this.settings.oberon, ROLE.OBERON)
    )

    for (role in evilRoles) {
        if (role.checked && needMoreEvil) {
            roles.add(role.type)
        }
    }

    while (needMoreEvil)
        roles.add(ROLE.MINION_OF_MORDRED)

}

private fun Lobby.addGood(roles: MutableList<ROLE>) {
    val numOfEvil = Constants.playerBalance[this.playerSize].evil

    fun needMoreGood() = (roles.size - numOfEvil) < Constants.playerBalance[this.playerSize].good
    val needMoreGood = needMoreGood()

    val goodRoles = listOf(
        SettingToRoleMapper(this.settings.percival, ROLE.PERCIVAL),
        SettingToRoleMapper(this.settings.arnold, ROLE.ARNOLD)
    )

    roles.add(ROLE.MERLIN)

    for (role in goodRoles) {
        if (role.checked && needMoreGood) {
            roles.add(role.type)
        }
    }

    while (needMoreGood)
        roles.add(ROLE.SERVANT_OF_ARTHUR)
}

private fun Lobby.assignRoleToPlayers(roles: List<ROLE>) {
    for (i in roles.indices) {
        this.userRoles.add(UserRoleMap(this.info.playersName[i], roles[i]))
    }
}

private fun Lobby.randomizeRoles() {
    val roles = mutableListOf<ROLE>()

    addEvil(roles)
    addGood(roles)
    roles.shuffle()

    if (roles.size != this.playerSize)
        ConflictException.Throw("roles.size != players.size")

    assignRoleToPlayers(roles)
}

private val Lobby.playerSize: Int
    get() = this.info.playersName.size


private fun Lobby.checkStartingConditions() {
    if (this.info.started)
        ConflictException.Throw("Lobby has already started")

    val minimumPlayersNeeded = 5

    if (this.info.playersName.size < minimumPlayersNeeded)
        ConflictException.Throw("Too few players")

    val maximumPlayers = 10

    if (this.playerSize > maximumPlayers)
        ConflictException.Throw("Too many players")
}

//NOTE: could be private at this time, but later we probably will use it as a public function
fun Lobby.setKing(playerName: String) {
    this.info.king = playerName
}


private fun Lobby.addEmptyVoteRoundPlaceholder() {
    this.votes.add(RoundVote("", mutableListOf(), mutableListOf()))
}

private fun Lobby.addNewVoteRound() {
    this.votes.add(RoundVote(this.info.king!!, mutableListOf(), mutableListOf()))
}

private fun Lobby.addEmptyAdventureVoteRoundPlaceholder() {
    this.adventureVotes.add(RoundVote("", mutableListOf(), mutableListOf()))
}

private fun Lobby.addNewAdventureVoteRound() {
    this.adventureVotes.add(RoundVote(this.info.king!!, currentChosen, mutableListOf()))
}

fun Lobby.start() {
    checkStartingConditions()

    this.info.started = true
    // might should be randomised
    val firstPlayerName = this.info.playersName[0]

    setKing(firstPlayerName)

    this.info.currentRound = 1

    addEmptyVoteRoundPlaceholder()
    addNewVoteRound()
    addEmptyAdventureVoteRoundPlaceholder()
    randomizeRoles()
}

private val Lobby.currentChosen: MutableList<String>
    get() = this.votes[this.info.currentRound].choosen

fun Lobby.select(chosen: List<String>) {
    if (chosen.size != Constants.getAdventreimit(playerSize, info.currentRound))
        ConflictException.Throw(
            "This is not the required amount of people! Required: ${
                Constants.getAdventreimit(
                    playerSize,
                    info.currentRound
                )
            }, but found : ${chosen.size}!"
        )

    if (chosen.isNotEmpty())
        ConflictException.Throw("The king has already chosen")

    currentChosen.addAll(chosen)

    this.info.selectedForAdventure = currentChosen

}

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

fun Lobby.vote(vote: SingleVote) {
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


private fun Lobby.nextRound() {
    this.info.currentRound++
    this.info.isAdventure = false
    nextKing()
    addNewVoteRound()
}

private fun Lobby.startAdventure() {
    this.info.failCounter = 0
    this.info.isAdventure = true
    this.info.currentAdventure++
    this.info.selectedForAdventure = currentChosen
    addNewAdventureVoteRound()
}

private fun Lobby.nextKing(): String {
    var idx = this.info.playersName.indexOf(this.info.playersName.first {
        this.info.king == it
    }) + 1

    if (idx == this.playerSize)
        idx = 0

    this.info.king = this.info.playersName[idx]

    return this.info.king!!
}

private val Lobby.currentAdvetureVotes: RoundVote
    get() = this.adventureVotes[this.info.currentAdventure]

private fun Lobby.checkIfChosen(vote: SingleVote) {
    currentAdvetureVotes.choosen.firstOrNull { vote.username == it }
        ?: ConflictException.Throw("You are not chosen")
}

private fun Lobby.checkIfAlreadyVotedOnAdventure(vote: SingleVote) {
    if (currentAdvetureVotes.results
            .firstOrNull { vote.username == it.username }
        != null
    )
        ConflictException.Throw("You already voted")
}

fun Lobby.voteOnAdventure(vote: SingleVote) {
    if (info.isAdventure.not())
        ConflictException.Throw("There is no adventure going on")

    checkIfChosen(vote)

    checkIfAlreadyVotedOnAdventure(vote)


    currentAdvetureVotes.results.add(vote)

    var limit = 1

    if (this.info.currentAdventure == 4) {
        limit = Constants.adventureLimit[this.playerSize].failsRequiredOnFourth
    }

    if (currentAdvetureVotes.results.size == currentAdvetureVotes.choosen.size) {
        if (currentAdvetureVotes.results.filter { !it.uservote }.size >= limit) {
            this.info.scores.add((this.info.currentAdventure - 1), SCORE.EVIL)
        } else {
            this.info.scores.add((this.info.currentAdventure - 1), SCORE.GOOD)
        }

        if (this.info.scores.filter { it == SCORE.EVIL }.size >= 3)
            this.info.winner = WINNER.EVIL

        if (this.info.scores.filter { it == SCORE.GOOD }.size >= 3)
            this.info.winner = WINNER.GOOD

        nextRound()
    }
}

private val Lobby.hasWinner: Boolean
    get() {
        if (this.info.winner != WINNER.NOT_DECIDED) {
            return true
        }
        return false
    }

fun Lobby.sees(role: ROLE, username: String): List<String> {
    if (role == ROLE.MERLIN) {
        return this.userRoles.filter {
            (it.role.isEvil() && it.role != ROLE.MORDRED)
        }.map { it.userName }.toList()
    }

    if (role == ROLE.OBERON)
        return this.userRoles.filter {
            (it.role.isEvil() && it.role != ROLE.OBERON && it.userName != username)
        }.map { it.userName }.toList()

    if (role.isEvil())
        return this.userRoles.filter {
            (it.role.isEvil() && it.role != ROLE.OBERON && it.userName != username)
        }.map { it.userName }.toList()

    if (role == ROLE.PERCIVAL)
        return this.userRoles.filter {
            (it.role == ROLE.MERLIN || it.role == ROLE.MORGANA)
        }.map { it.userName }.toList()


    return listOf()
}
package blu3berry.why.avalon.dal.extensions

import blu3berry.why.avalon.model.helpers.Constants
import blu3berry.why.avalon.model.db.Lobby
import blu3berry.why.avalon.model.db.UserRoleMap
import blu3berry.why.avalon.model.enums.ROLE
import blu3berry.why.avalon.model.enums.SCORE
import blu3berry.why.avalon.model.enums.WINNER
import blu3berry.why.avalon.model.network.RoundVote
import blu3berry.why.avalon.model.network.SingleVote

fun Lobby.randomizeRoles(){
    val roles = mutableListOf<ROLE>()
    val numOfEvil = Constants.playerBalance[this.playerSize].evil

    val needMoreEvil = fun(): Boolean{
        return roles.size < numOfEvil
    }()

    val evilRolesSettings = listOf(this.settings.assassin,this.settings.morgana,this.settings.mordred,this.settings.oberon);
    val evilRoles = listOf(ROLE.ASSASSIN,ROLE.MORGANA,ROLE.MORDRED,ROLE.OBERON);

    for (i in 0 until 4){
        if (evilRolesSettings[i] && needMoreEvil){
            roles.add(evilRoles[i])
        }
    }

    while (needMoreEvil)
        roles.add(ROLE.MINION_OF_MORDRED)

    val needMoreGood= fun(): Boolean{
        return (roles.size - numOfEvil) < Constants.playerBalance[this.playerSize].good
    }()

    roles.add(ROLE.MERLIN)

    if(this.settings.percival && needMoreGood)
        roles.add(ROLE.PERCIVAL)

    if (this.settings.arnold && needMoreGood)
        roles.add(ROLE.ARNOLD)

    while (needMoreGood)
        roles.add(ROLE.MINION_OF_MORDRED)

    roles.shuffle()

    if (roles.size != this.playerSize)
        throw IllegalArgumentException("Randomize.kt : 58, roles.size != players.size")

    for (i in 0 until roles.size){
        this.userRoles.add(UserRoleMap(this.info.playersName[i], roles[i]))
    }
}

val Lobby.playerSize: Int
    get() = this.info.playersName.size


fun Lobby.start() {
    if (this.info.started)
        throw IllegalArgumentException("Lobby has already started")

    if (this.info.playersName.size < 5)
        throw IllegalArgumentException("Too few players")

    if (this.info.playersName.size > 10)
        throw IllegalArgumentException("Too many players")

    this.info.started = true
    // might should be randomised
    this.info.king = this.info.playersName[0]
    this.info.currentRound = 1
    // round 0 is just a placeholder
    this.votes.add(RoundVote("", mutableListOf(), mutableListOf()))
    this.votes.add(RoundVote(this.info.king!!, mutableListOf(), mutableListOf()))
    this.adventureVotes.add(RoundVote("", mutableListOf(), mutableListOf()))
    this.randomizeRoles()
}

fun Lobby.select(chosen: List<String>) {
    if (chosen.size != Constants.adventureLimit[this.playerSize].limits[this.info.currentRound])
        throw IllegalArgumentException("This is not the required amount of people! Required: ${Constants.adventureLimit[this.playerSize].limits[this.info.currentRound]}, but found : ${chosen.size}!")

    if (chosen.isNotEmpty())
        throw IllegalArgumentException("The king has already chosen")

    chosen.forEach {
        this.votes[this.info.currentRound].choosen.add(it)
    }

    this.info.selectedForAdventure = this.votes[this.info.currentRound].choosen

}

fun Lobby.vote(vote: SingleVote) {
    val results = this.votes[this.info.currentRound].results
    results.firstOrNull {
        it.username == vote.username
    } ?: results.add(vote)

    if (results.size == this.info.playersName.size) {
        if (results.filter { it.uservote }.size > (this.info.playersName.size / 2)) {
            this.startAdventure()
            this.info.failCounter = 0
        } else {
            this.info.failCounter++
            if (this.info.failCounter == 5) {
                this.info.winner = WINNER.EVIL
            }
            this.nextRound()
        }
    }

}

fun Lobby.nextRound() {
    this.info.currentRound++
    this.info.isAdventure = false
    this.votes.add(RoundVote(this.nextKing(), mutableListOf(), mutableListOf()))
}

fun Lobby.startAdventure() {
    this.info.isAdventure = true
    this.info.currentAdventure++
    this.info.selectedForAdventure = this.votes[this.info.currentRound].choosen
    this.adventureVotes.add(RoundVote(this.info.king!!, this.votes[this.info.currentRound].choosen, mutableListOf()))
}

fun Lobby.nextKing(): String {
    var idx = this.info.playersName.indexOf(this.info.playersName.first {
        this.info.king == it
    }) + 1

    if (idx == this.info.playersName.size)
        idx = 0

    this.info.king = this.info.playersName[idx]

    return this.info.king!!
}

fun Lobby.voteOnAdventure(vote: SingleVote) {
    if (!this.info.isAdventure)
        throw IllegalArgumentException("There is no adventure going on")

    val round = this.adventureVotes[this.info.currentAdventure]

    if (round.choosen.firstOrNull { vote.username == it } == null)
        throw IllegalArgumentException("You are not chosen")

    if (round.results.firstOrNull { vote.username == it.username } != null)
        throw IllegalArgumentException("You already voted")

    round.results.add(vote)

    var limit = 1

    if (this.info.currentAdventure == 4) {
        limit = Constants.adventureLimit[this.playerSize].failsRequiredOnFourth
    }

    if (round.results.size == round.choosen.size) {
        if (round.results.filter { !it.uservote }.size >= limit) {
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

val Lobby.hasWinner: Boolean
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
package blu3berry.why.avalon.model.db.lobby.extensions

import blu3berry.why.avalon.api.errorhandling.exceptions.ConflictException
import blu3berry.why.avalon.model.db.lobby.Lobby
import blu3berry.why.avalon.model.db.UserRoleMap
import blu3berry.why.avalon.model.enums.ROLE
import blu3berry.why.avalon.model.helpers.Constants
import blu3berry.why.avalon.model.network.RoundVote

data class SettingToRoleMapper(val checked: Boolean, val type: ROLE)

private val Lobby.evilSettingsToRolesMap: List<SettingToRoleMapper>
    get() = listOf(
        SettingToRoleMapper(this.settings.assassin, ROLE.ASSASSIN),
        SettingToRoleMapper(this.settings.morgana, ROLE.MORGANA),
        SettingToRoleMapper(this.settings.mordred, ROLE.MORDRED),
        SettingToRoleMapper(this.settings.oberon, ROLE.OBERON)
    )

private val Lobby.numberOfEvilPlayers: Int
    get() = Constants.playerBalance[this.playerSize].evil

private fun Lobby.addEvil(roles: MutableList<ROLE>) {
    fun needMoreEvil() = roles.size < numberOfEvilPlayers
    val needMoreEvil = needMoreEvil()

    for (role in evilSettingsToRolesMap) {
        if (role.checked && needMoreEvil) {
            roles.add(role.type)
        }
    }

    while (needMoreEvil)
        roles.add(ROLE.MINION_OF_MORDRED)

}

private val Lobby.goodSettingsToRoleMap: List<SettingToRoleMapper>
    get() = listOf(
        SettingToRoleMapper(this.settings.percival, ROLE.PERCIVAL),
        SettingToRoleMapper(this.settings.arnold, ROLE.ARNOLD)
    )

private fun Lobby.addGood(roles: MutableList<ROLE>) {

    fun needMoreGood() = (roles.size - numberOfEvilPlayers) < Constants.playerBalance[this.playerSize].good
    val needMoreGood = needMoreGood()

    roles.add(ROLE.MERLIN)

    for (role in goodSettingsToRoleMap) {
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




private fun Lobby.addEmptyVoteRoundPlaceholder() {
    this.votes.add(RoundVote("", mutableListOf(), mutableListOf()))
}



private fun Lobby.addEmptyAdventureVoteRoundPlaceholder() {
    this.adventureVotes.add(RoundVote("", mutableListOf(), mutableListOf()))
}



internal fun Lobby.start_impl() {
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
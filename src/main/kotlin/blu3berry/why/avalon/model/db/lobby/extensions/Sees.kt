package blu3berry.why.avalon.model.db.lobby.extensions

import blu3berry.why.avalon.model.db.lobby.Lobby
import blu3berry.why.avalon.model.db.UserRoleMap
import blu3berry.why.avalon.model.enums.ROLE

private fun Lobby.getAllEvilUsers(): List<UserRoleMap> {
    return this.userRoles.filter {
        it.role.isEvil
    }
}

private fun List<UserRoleMap>.mapToUsername() =
    this.map { it.userName }.toList()


internal fun Lobby.sees_impl(role: ROLE, username: String): List<String> {
    return when {
        role.isMerlin -> getAllEvilUsers().filter { it.role.isMordred.not() }
        role.isOberon -> getAllEvilUsers().filter { it.role.isOberon.not() }
        role.isEvil -> getAllEvilUsers().filter { it.role.isOberon.not() && it.userName != username }
        role.isPercival -> userRoles.filter { it.role.looksLikeMerlin }
        else -> listOf()
    }.mapToUsername()
}
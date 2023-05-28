package blu3berry.why.avalon.mockdata.builders

import blu3berry.why.avalon.model.db.UserRoleMap
import blu3berry.why.avalon.model.enums.ROLE

class UserRoleMapBuilder {
    var username: String = ""
    var role: ROLE = ROLE.SERVANT_OF_ARTHUR

    fun build(): UserRoleMap {
        return UserRoleMap(username, role)
    }
}
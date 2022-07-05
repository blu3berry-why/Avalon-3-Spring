package blu3berry.why.avalon.model.network

import blu3berry.why.avalon.model.enums.ROLE

data class CharacterInfo(
    var name: ROLE,
    val sees: List<String>,
)

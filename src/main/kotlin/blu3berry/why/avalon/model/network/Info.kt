package blu3berry.why.avalon.model.network

import blu3berry.why.avalon.model.enums.SCORE
import blu3berry.why.avalon.model.enums.WINNER

data class Info(
    var started :Boolean,
    var winner: WINNER = WINNER.NOT_DECIDED,
    var scores: MutableList<SCORE>,
    var currentRound: Int,
    var isAdventure: Boolean,
    var currentAdventure: Int,
    var king: String?,
    var failCounter: Int = 0,
    var selectedForAdventure: MutableList<String>,
    var playersName: MutableList<String>,
    var assassinHasGuessed:Boolean = false,
    var playerSelectNum: Int = 0,
)

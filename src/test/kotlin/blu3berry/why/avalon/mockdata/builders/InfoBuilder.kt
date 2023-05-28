package blu3berry.why.avalon.mockdata.builders

import blu3berry.why.avalon.model.enums.SCORE
import blu3berry.why.avalon.model.enums.WINNER
import blu3berry.why.avalon.model.network.Info

class InfoBuilder {
    var started: Boolean = true
    var winner: WINNER = WINNER.NOT_DECIDED
    var scores: MutableList<SCORE> = mutableListOf(SCORE.UNDECIDED,SCORE.UNDECIDED,SCORE.UNDECIDED,SCORE.UNDECIDED,SCORE.UNDECIDED)
    var currentRound: Int = 0
    var isAdventure: Boolean = false
    var currentAdventure: Int = 0
    var king: String = ""
    var failCounter: Int = 0
    var selectedForAdventure: MutableList<String> = mutableListOf()
    var playersName: MutableList<String> = mutableListOf()
    var assassinHasGuessed: Boolean = false
    var playerSelectNum: Int = 0

    fun build(): Info {
        return Info(
            started,
            winner,
            scores,
            currentRound,
            isAdventure,
            currentAdventure,
            king,
            failCounter,
            selectedForAdventure,
            playersName,
            assassinHasGuessed,
            playerSelectNum
        )
    }
}
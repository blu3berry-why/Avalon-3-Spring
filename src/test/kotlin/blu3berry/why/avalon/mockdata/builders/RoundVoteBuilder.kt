package blu3berry.why.avalon.mockdata.builders

import blu3berry.why.avalon.model.network.RoundVote
import blu3berry.why.avalon.model.network.SingleVote

class RoundVoteBuilder {
    var king: String = ""
    var chosen: MutableList<String> = mutableListOf()
    var results: MutableList<SingleVote> = mutableListOf()

    fun build(): RoundVote {
        return RoundVote(
            king,
            chosen,
            results
        )
    }
}

package blu3berry.why.avalon.converters

import blu3berry.why.avalon.model.network.SingleVote

fun parseVotes(votesString: String): MutableList<SingleVote> {
    if (votesString.contains("empty")) return mutableListOf()

    return votesString.split(";")
        .map {
            val (username, voteValue) = it.split("=")
            SingleVote(username, voteValue.toBoolean())
        }.toMutableList()
}
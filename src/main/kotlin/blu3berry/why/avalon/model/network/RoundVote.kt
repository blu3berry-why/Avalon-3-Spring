package blu3berry.why.avalon.model.network


data class RoundVote(
    val king:String,
    val choosen: MutableList<String>,
    val results: MutableList <SingleVote>,
)

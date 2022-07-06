package blu3berry.why.avalon.model.helpers

data class AdventureLimit(
    val valid: Boolean = true,
    val limits: List<Int> = listOf(),
    val failsRequiredOnFourth:Int = 1,

)

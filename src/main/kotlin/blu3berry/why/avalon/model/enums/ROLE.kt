package blu3berry.why.avalon.model.enums

enum class ROLE {
    PERCIVAL {
        override val isEvil = false
    },
    MERLIN {
        override val isEvil = false
    },
    SERVANT_OF_ARTHUR {
        override val isEvil = false
    },
    ARNOLD {
        override val isEvil = false
    },
    ASSASSIN {
        override val isEvil = true
    },
    MORGANA {
        override val isEvil = true
    },
    MORDRED {
        override val isEvil = true
    },
    OBERON {
        override val isEvil = true
    },
    MINION_OF_MORDRED {
        override val isEvil = true

    }


    ; // - one of the needed commas

    //abstract fun isEvil(): Boolean

    abstract val isEvil : Boolean

    companion object{
        fun getAllEvil(): List<ROLE> {
            return listOf(ASSASSIN, MORGANA, MORDRED, OBERON, MINION_OF_MORDRED)
        }

        fun getAllGood():List<ROLE>{
            return listOf(PERCIVAL, MERLIN, MINION_OF_MORDRED, ARNOLD)
        }
    }

    val isOberon: Boolean
        get() = this == OBERON

    val isMordred: Boolean
        get() = this == MORDRED

    val isMerlin: Boolean
        get() = this == MERLIN

    val isPercival: Boolean
        get() = this == PERCIVAL

    val looksLikeMerlin: Boolean
        get() =
            this == MERLIN || this == MORGANA

}
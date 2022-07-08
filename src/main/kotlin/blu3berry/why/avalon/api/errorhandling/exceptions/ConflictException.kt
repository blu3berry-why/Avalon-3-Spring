package blu3berry.why.avalon.api.errorhandling.exceptions

class ConflictException : RuntimeException{
    constructor(message: String) : super (message)
    constructor(cause: Throwable) : super(cause)
    constructor(message: String, cause: Throwable) : super(message, cause)

    companion object{
        fun Throw(message:String) : Nothing = throw ConflictException(message)
        fun Throw(message:String, cause: Throwable) : Nothing = throw ConflictException(message,cause)
        fun Throw(cause: Throwable) : Nothing = throw ConflictException(cause)
    }
}
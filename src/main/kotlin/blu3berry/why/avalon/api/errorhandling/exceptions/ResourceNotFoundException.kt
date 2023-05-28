package blu3berry.why.avalon.api.errorhandling.exceptions

class ResourceNotFoundException : RuntimeException{
    constructor(message: String) : super (message)
    constructor(cause: Throwable) : super(cause)
    constructor(message: String, cause: Throwable) : super(message, cause)

    companion object{
        val lobbyNotFound: Nothing
            get() = throw ResourceNotFoundException("Lobby with this code does not exists.")
    }
}
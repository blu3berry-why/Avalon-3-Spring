package blu3berry.why.avalon.model.network

data class Message(
    var message: String
){
    companion object{
        val OK: Message
            get() = Message("OK")
    }
}




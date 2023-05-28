package blu3berry.why.avalon.firebase

abstract class AppNotification(open val title: String, open val message: String)

data class TopicNotification(val topic: String, override val title: String,
                             override val message: String): AppNotification(title, message)

data class DirectNotification(val target: String, override val title: String,
                              override val message: String): AppNotification(title, message)
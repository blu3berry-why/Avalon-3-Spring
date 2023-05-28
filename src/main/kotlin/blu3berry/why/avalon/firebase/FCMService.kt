package blu3berry.why.avalon.firebase

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.WebpushConfig
import com.google.firebase.messaging.WebpushNotification
import org.springframework.stereotype.Service

@Service
class FCMService {
   fun sendNotificationToTarget(notification: DirectNotification){
       val message = Message.builder()
                // Set the configuration for our web notification
               .setWebpushConfig(
                       // Create and pass a WebpushConfig object setting the notification
                       WebpushConfig.builder()
                               .setNotification(
                                       // Create and pass a web notification object with the specified title, body, and icon URL 
                                       WebpushNotification.builder()
                                               .setTitle(notification.title)
                                               .setBody(notification.message)
                                               .setIcon("https://assets.mapquestapi.com/icon/v2/circle@2x.png")
                                               .build()
                               ).build()
               )
                // Specify the user to send it to in the form of their token  
               .setToken(notification.target)
               .build()
       FirebaseMessaging.getInstance().sendAsync(message)
   }

    // Same code as above, the only difference is we call setTopic instead of setToken with the appropriate topic
    fun sendNotificationToTopic(notification: TopicNotification){
        val message = Message.builder()
            .setWebpushConfig(
                WebpushConfig.builder()
                    .setNotification(
                        WebpushNotification.builder()
                            .setTitle(notification.title)
                            .setBody(notification.message)
                            .setIcon("https://assets.mapquestapi.com/icon/v2/incident@2x.png")
                            .build()
                    ).build()
            ).setTopic(notification.topic)
            .build()

        FirebaseMessaging.getInstance().sendAsync(message)
    }

    fun subscribeToTopic(subscription: SubscriptionRequest){

        FirebaseMessaging.getInstance().subscribeToTopic(listOf(subscription.subscriber), subscription.topic)

    }
}
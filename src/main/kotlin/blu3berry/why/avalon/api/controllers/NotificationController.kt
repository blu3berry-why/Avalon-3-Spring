package blu3berry.why.avalon.api.controllers

import blu3berry.why.avalon.firebase.DirectNotification
import blu3berry.why.avalon.firebase.FCMService
import blu3berry.why.avalon.firebase.SubscriptionRequest
import blu3berry.why.avalon.firebase.TopicNotification
import io.swagger.v3.oas.annotations.Hidden
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

// Hidden from the client contract: server-to-Firebase push, not called by the KMP client (F4).
@Hidden
@RestController
class NotificationController(private val fcm: FCMService) {
   @PostMapping("/notification")
   fun sendTargetedNotification(@RequestBody notification: DirectNotification){
       fcm.sendNotificationToTarget(notification)
   }

   @PostMapping("/topic/notification")
   fun sendNotificationToTopic(@RequestBody notification: TopicNotification){
       fcm.sendNotificationToTopic(notification)
   }

   @PostMapping("/topic/subscription")
   fun subscribeToTopic(@RequestBody subscription: SubscriptionRequest){
       fcm.subscribeToTopic(subscription)
   }
}
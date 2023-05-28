package blu3berry.why.avalon.firebase

// The subscriber field specifies the token of the subscribing user
data class SubscriptionRequest(val subscriber: String, val topic: String)
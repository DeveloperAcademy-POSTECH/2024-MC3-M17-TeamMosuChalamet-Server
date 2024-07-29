//package com.be.shoackserver.application.service
//
//import com.eatthepath.pushy.apns.ApnsClient
//import com.eatthepath.pushy.apns.PushNotificationResponse
//import com.eatthepath.pushy.apns.util.ApnsPayloadBuilder
//import com.eatthepath.pushy.apns.util.SimpleApnsPayloadBuilder
//import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification
//import com.eatthepath.pushy.apns.util.TokenUtil
//import org.springframework.stereotype.Service
//import java.time.Instant
//import java.util.concurrent.CompletableFuture
//
//@Service
//class AsyncMessageService(
//    private val apnsClient: ApnsClient
//) {
//    fun sendPushNotifications(destinationMemberIds: List<Long>) {
//        val notifications = createNotifications(destinationMemberIds)
//
//        for (pushNotification in notifications) {
//            val sendNotificationFuture: CompletableFuture<PushNotificationResponse<SimpleApnsPushNotification>> =
//                apnsClient.sendNotification(pushNotification)
//
//            sendNotificationFuture.whenComplete { response, cause ->
//                if (cause != null) {
//                    handlePushNotificationError(cause)
//                } else {
//                    handlePushNotificationResponse(response)
//                }
//            }
//        }
//    }
//
//    private fun createNotifications(memberIds: List<Long>): List<SimpleApnsPushNotification> {
//        val payloadBuilder: ApnsPayloadBuilder = SimpleApnsPayloadBuilder()
//        payloadBuilder.setAlertTitle("Test Title")
//        payloadBuilder.setAlertBody("Test Body: Notification")
//        val payload = payloadBuilder.build()
//
//        return memberIds.map { memberId ->
//
//            val token = TokenUtil.sanitizeTokenString("<efc7492 bdbd8209>")
//            SimpleApnsPushNotification(token, "my_App_bundle_id", payload)
//        }
//    }
//
//    private fun handlePushNotificationResponse(
//        response: PushNotificationResponse<SimpleApnsPushNotification>
//    ) {
//        if (response.isAccepted) {
//            println("Push notification accepted by APNs gateway.")
//            println("Message : ${response.pushNotification.payload}")
//        } else {
//            println("Notification rejected by the APNs gateway: ${response.rejectionReason}")
//            response.tokenInvalidationTimestamp.ifPresent { timestamp: Instant ->
//                println("\t…and the token is invalid as of $timestamp")
//            }
//        }
//    }
//
//    private fun handlePushNotificationError(cause: Throwable) {
//        System.err.println("Failed to send push notification.")
//        cause.printStackTrace()
//    }
//}
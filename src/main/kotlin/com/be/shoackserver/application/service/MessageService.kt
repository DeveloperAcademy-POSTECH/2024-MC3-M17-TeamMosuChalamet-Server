package com.be.shoackserver.application.service

import com.be.shoackserver.application.dto.MemberDto
import com.be.shoackserver.application.usecase.MessageUseCase
import com.eatthepath.pushy.apns.ApnsClient
import com.eatthepath.pushy.apns.PushNotificationResponse
import com.eatthepath.pushy.apns.util.ApnsPayloadBuilder
import com.eatthepath.pushy.apns.util.SimpleApnsPayloadBuilder
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification
import com.eatthepath.pushy.apns.util.TokenUtil
import com.eatthepath.pushy.apns.util.concurrent.PushNotificationFuture
import com.fasterxml.jackson.databind.util.JSONPObject
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.springframework.stereotype.Service

import java.util.concurrent.ExecutionException


@Service
class MessageService(
    private val apnsClient: ApnsClient
) {
    fun sendPushNotification(senderInfo: MessageUseCase.SenderInfo) {

        // SimpleApnsPayloadBuilder를 사용하여 Gson or Jackson 기반으로 payload 생성
        val payloadBuilder: ApnsPayloadBuilder = SimpleApnsPayloadBuilder()
        payloadBuilder.setAlertTitle("Test Title")
        payloadBuilder.setAlertBody("Test Body: Notification")

        // 송신자 정보 추가
        val senderInfoJson : JsonObject = JsonObject().apply {
            addProperty("senderId", senderInfo.senderId)
            addProperty("senderName", senderInfo.senderName)
            addProperty("senderImageURL", senderInfo.senderImageURL)
        }

        payloadBuilder.addCustomProperty("senderInfo", senderInfo)

        val payload = payloadBuilder.build()
        val token = TokenUtil.sanitizeTokenString("apns_device_token") // senderInfo.deviceToken 사용해야 함
        val pushNotification = SimpleApnsPushNotification(token, "my_App_bundle_id", payload)

        /*
        * PushNotificationFuture: PushNotificationResponse<SimpleApnsPushNotification>를 반환하는 CompletableFuture를 구현한 인터페이스
        * */
        val sendNotificationFuture: PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>> =
            apnsClient.sendNotification(pushNotification)

        try {
            val response =
                sendNotificationFuture.get()

            if (response.isAccepted) {
                println("Push notification is accepted")
                println("Message : " + response.pushNotification.payload)
            } else {
                println(
                    "Notification is rejected : " +
                            response.rejectionReason
                )
            }
        } catch (e: ExecutionException) {
            System.err.println("Failed to send push notification.")
            e.printStackTrace()
        }
//        비동기 처리
//        sendNotificationFuture.whenComplete {response, throwable ->
//            if (throwable != null) {
//                System.err.println("Failed to send push notification.")
//                throwable.printStackTrace()
//            }
//            else {
//                if (response.isAccepted) {
//                    println("Push notification accepted by APNs gateway.")
//                    println("Message : ${response.pushNotification.payload}")
//                } else {
//                    println(
//                        "Notification is rejected : " +
//                                response.rejectionReason
//                    )
//                }
//            }
//        }

    }
}

/*
func application(_ application: UIApplication,
didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
    Messaging.messaging().apnsToken = deviceToken
    let token = deviceToken as NSData
            let token2 = token.map{String(format:"%02x",$0)}.joined()
    print("디바이스 토큰 : ", token2)
}*/

package com.be.shoackserver.application.service

import com.be.shoackserver.application.dto.MemberDto
import com.eatthepath.pushy.apns.ApnsClient
import com.eatthepath.pushy.apns.PushNotificationResponse
import com.eatthepath.pushy.apns.util.ApnsPayloadBuilder
import com.eatthepath.pushy.apns.util.SimpleApnsPayloadBuilder
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification
import com.eatthepath.pushy.apns.util.TokenUtil
import com.eatthepath.pushy.apns.util.concurrent.PushNotificationFuture
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Service
class MessageService(
    private val apnsClient: ApnsClient,
    @Value("\${oauth.apple.watch-client-id}") private val watchBundleId: String
) {

    private val log : Logger = LogManager.getLogger(MessageService::class.java)

    fun sendPushNotification(
        memberDto: MemberDto,
        destinationDeviceToken: String,
        topic: String
    ) {
        // SimpleApnsPayloadBuilder를 사용하여 Gson or Jackson 기반으로 payload 생성
        val payloadBuilder: ApnsPayloadBuilder = SimpleApnsPayloadBuilder()
        payloadBuilder.setAlertTitle(memberDto.name) // 송신자의 이름
        payloadBuilder.setAlertBody("쇽! 날 봐줘!") // 메시지 내용
        payloadBuilder.setSound("default")
        payloadBuilder.setCategoryName("shoakreceive")

        payloadBuilder.addCustomProperty("profileName", memberDto.name)
        payloadBuilder.addCustomProperty("profileImageURL", memberDto.imageName)
        payloadBuilder.addCustomProperty("message", "쇽! 날 봐줘!")
        payloadBuilder.addCustomProperty("Simulator Target Bundle", watchBundleId)

        val payload = payloadBuilder.build()
        val token = TokenUtil.sanitizeTokenString(destinationDeviceToken)

        val pushNotification = SimpleApnsPushNotification(token, topic, payload)

        /*
        * PushNotificationFuture: PushNotificationResponse<SimpleApnsPushNotification>를 반환하는 CompletableFuture를 구현한 인터페이스
        * */
        val sendNotificationFuture: PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>> =
            apnsClient.sendNotification(pushNotification)

        sendNotificationFuture.whenComplete {response, throwable ->
            if (throwable != null) {
                log.error("Failed to send push notification")
                throwable.printStackTrace()
            }
            else {
                if (response.isAccepted) {
                    log.info("response: " + response.pushNotification)
                    log.info("Push notification accepted by APNs gateway")
                    log.info("Message : ${response.pushNotification.payload}")

                    println("response: " + response.pushNotification)
                    println("Push notification accepted by APNs gateway")
                    println("Message : ${response.pushNotification.payload}")
                } else {
                    log.error("response: " + response.pushNotification)
                    log.error(
                        "Notification is rejected : " +
                                response.rejectionReason
                    )
                    println("response: " + response.pushNotification)
                    println(
                        "Notification is rejected : " +
                                response.rejectionReason
                    )
                }
            }
        }


    }
}


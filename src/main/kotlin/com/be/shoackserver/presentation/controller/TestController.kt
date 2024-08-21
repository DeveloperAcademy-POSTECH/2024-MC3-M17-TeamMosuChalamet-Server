package com.be.shoackserver.presentation.controller

import com.be.shoackserver.application.dto.MemberDto
import com.be.shoackserver.application.service.MessageService
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController (
    private val messageService: MessageService,
    @Value("\${oauth.apple.appClip-client-id}") private val appClipClientId: String,
    @Value("\${oauth.apple.client-id}") private val clientId: String,
) {

    @GetMapping("app_clip/push_notification")
    fun pushNotificationTestForAppClip(@RequestParam deviceToken : String) {
        val memberDto = MemberDto(1L, "appleUserId", "test user", "imageName", "deviceToken", "appleRefreshToken", "userAgent", "role")
        val topic = appClipClientId
        messageService.sendPushNotification(memberDto, deviceToken, topic)
    }

    @GetMapping("app/push_notification")
    fun pushNotificationTestForApp(@RequestParam deviceToken : String) {
        val memberDto = MemberDto(1L, "appleUserId", "test user", "imageName", "deviceToken", "appleRefreshToken", "userAgent", "role")
        val topic = clientId
        messageService.sendPushNotification(memberDto, deviceToken, topic)
    }
}
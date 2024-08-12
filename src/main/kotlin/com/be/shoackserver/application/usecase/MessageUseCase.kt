package com.be.shoackserver.application.usecase

import com.be.shoackserver.application.dto.MemberDto
import com.be.shoackserver.application.service.AuthenticationService
import com.be.shoackserver.application.service.ImageService
import com.be.shoackserver.application.service.MemberService
import com.be.shoackserver.application.service.MessageService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class MessageUseCase(
    @Value("\${oauth.apple.client-id}") private val clientId: String,
    @Value("\${oauth.apple.appClip-client-id}") private val appClipClientId: String,
    private val messageService: MessageService,
    private val memberService: MemberService,
    private val imageService: ImageService,
    private val authenticationService: AuthenticationService

) {
    private fun getMemberId() : Long {
        return authenticationService.getMemberIdFromSecurityContext()
    }

    fun sendMessage(destinationMemberId: Long) {
        val memberDto = MemberDto.of(memberService.findMemberById(getMemberId()))
        memberDto.imageName = memberDto.imageName?.let {imageService.generateS3URL("profile", it)}
        val destinationDeviceToken = memberService.findDeviceTokenByMemberId(destinationMemberId)
        val destinationMemberDto = MemberDto.of(memberService.findMemberById(destinationMemberId))
        val userAgent =destinationMemberDto.userAgent
        when (userAgent) {
            "appClip" -> {
                messageService.sendPushNotification(memberDto, destinationDeviceToken, appClipClientId)
            }
            else -> {
                messageService.sendPushNotification(memberDto, destinationDeviceToken, clientId)
            }
        }

    }
}
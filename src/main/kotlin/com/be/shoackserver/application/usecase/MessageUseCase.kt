package com.be.shoackserver.application.usecase

import com.be.shoackserver.application.dto.MemberDto
import com.be.shoackserver.application.service.AuthenticationService
import com.be.shoackserver.application.service.ImageService
import com.be.shoackserver.application.service.MemberService
import com.be.shoackserver.application.service.MessageService
import org.springframework.stereotype.Service

@Service
class MessageUseCase(
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
        messageService.sendPushNotification(memberDto, destinationDeviceToken)
    }
}
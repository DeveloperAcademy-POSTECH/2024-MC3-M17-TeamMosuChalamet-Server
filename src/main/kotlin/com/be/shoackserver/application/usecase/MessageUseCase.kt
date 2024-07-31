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
    data class SenderInfo(
        val senderId: Long,
        val senderName: String,
        val senderImageURL: String,
        val senderDeviceToken: String
    )
    fun sendMessage(destinationMemberId: Long) {
        val memberDto = MemberDto.of(memberService.findMemberById(getMemberId()))
        val destinationMemberDto = MemberDto.of(memberService.findMemberById(destinationMemberId))
        messageService.sendPushNotification(memberDto, destinationMemberDto)
    }

    private fun toSenderInfo (memberDto: MemberDto) : SenderInfo {
        val id = memberDto.id ?: throw IllegalStateException("Member id is null")
        val name = memberDto.name ?: throw IllegalStateException("Member name is null")
        val imageURL = imageService.generateS3URL(
            "profile",
            memberDto.imageName ?: throw IllegalStateException("Image name is null")
        )
        val deviceToken = memberDto.deviceToken ?: throw IllegalStateException("Device token is null")

        return SenderInfo(id, name, imageURL, deviceToken)
    }
}
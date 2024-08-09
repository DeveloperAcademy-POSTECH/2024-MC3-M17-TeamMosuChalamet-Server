package com.be.shoackserver.application.usecase

import com.be.shoackserver.application.dto.MemberDto
import com.be.shoackserver.application.service.AppleAuthService
import com.be.shoackserver.application.service.AuthenticationService
import com.be.shoackserver.application.service.MemberService
import com.be.shoackserver.domain.entity.Member
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class MemberManageUseCase(
    private val memberService: MemberService,
    private val appleAuthService: AppleAuthService,
    private val authenticationService: AuthenticationService
) {

    private fun getMemberId() : Long {
        return authenticationService.getMemberIdFromSecurityContext()
    }

    fun addNewMember(appleUserId: String, name: String, deviceToken: String) : MemberDto {
        val memberDto = MemberDto()
        memberDto.appleUserId = appleUserId
        memberDto.name = name
        memberDto.deviceToken = deviceToken
        memberDto.role = "ROLE_USER"
        val member: Member = memberService.saveMember(Member.create(memberDto))
        return MemberDto.of(member)
    }

    fun updateDeviceToken(deviceToken: String) {
        memberService.updateMemberDeviceToken(getMemberId(), deviceToken)
    }

    fun saveAppleRefreshToken(memberId: Long, authorizationCode: String) {
        memberService.saveRefreshToken(memberId, appleAuthService.getAppleTokens(authorizationCode).refresh_token)
    }

    fun deleteMember() {
        val memberDto = MemberDto.of(memberService.getMember(getMemberId()))
        val refreshToken = memberDto.appleRefreshToken ?: throw IllegalArgumentException("refreshToken is null")
        appleAuthService.requestToRevokeAppleToken(refreshToken) // Apple 서버에 회원 탈퇴 요청
        memberService.deleteMember(getMemberId()) // DB 에서 회원 삭제
    }
}

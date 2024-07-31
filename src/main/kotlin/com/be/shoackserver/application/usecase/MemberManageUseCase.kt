package com.be.shoackserver.application.usecase

import com.be.shoackserver.application.dto.MemberDto
import com.be.shoackserver.application.service.MemberService
import com.be.shoackserver.domain.entity.Member
import org.springframework.stereotype.Service

@Service
class MemberManageUseCase(
    private val memberService: MemberService
) {
    fun addNewMember(appleUserId: String, name: String, deviceToken: String) : MemberDto {
        val memberDto : MemberDto = MemberDto()
        memberDto.appleUserId = appleUserId
        memberDto.name = name
        memberDto.deviceToken = deviceToken
        memberDto.role = "ROLE_USER"
        val member: Member = memberService.saveMember(Member.create(memberDto))
        return MemberDto.of(member)
    }
}
package com.be.shoackserver.application.service

import com.be.shoackserver.application.dto.MemberDto
import com.be.shoackserver.application.exception.MemberNotFoundException
import com.be.shoackserver.domain.entity.Member
import com.be.shoackserver.domain.repository.MemberRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class MemberService (
    private val memberRepository: MemberRepository
) {

    fun getMember(memberId: Long) : Member {
        return memberRepository.findById(memberId)
            .orElseThrow() { MemberNotFoundException(memberId) }
    }

    fun findMemberById(memberId: Long) : Member {
        return memberRepository.findById(memberId)
            .orElseThrow() { MemberNotFoundException(memberId) }
    }

    @Transactional
    fun updateMemberProfileImageName(imageName: String, memberId: Long) {
        val member = memberRepository.findById(memberId)
            .orElseThrow() {MemberNotFoundException(memberId)}
        member.imageName = imageName
    }

    fun findDeviceTokenByMemberId(memberId: Long) : String {
        return memberRepository.findById(memberId)
            .orElseThrow() { MemberNotFoundException(memberId) }
            .deviceToken ?: throw IllegalArgumentException("Device token is null")
    }

    fun saveMember(member: Member) : Member {
        return memberRepository.save(member)
    }
}
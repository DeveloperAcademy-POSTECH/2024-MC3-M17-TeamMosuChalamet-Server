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
    private val memberId: Long = 1

    fun getMember() : Member {
        return memberRepository.findById(memberId)
            .orElseThrow() { MemberNotFoundException(memberId) }
    }

    @Transactional
    fun updateMemberProfileImageName(imageName: String) {
        val member = memberRepository.findById(memberId)
            .orElseThrow() {MemberNotFoundException(memberId)}
        member.imageName = imageName
    }
}
package com.be.shoackserver.application.service

import com.be.shoackserver.application.dto.MemberDto
import com.be.shoackserver.application.exception.MemberNotFoundException
import com.be.shoackserver.domain.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class MemberService (
    private val memberRepository: MemberRepository
) {
    private val memberId: Long = 1

    fun getMember() : MemberDto {
        return memberRepository.findById(memberId)
            .map { MemberDto.of(it) }
            .orElseThrow() { MemberNotFoundException(memberId) }
    }
}
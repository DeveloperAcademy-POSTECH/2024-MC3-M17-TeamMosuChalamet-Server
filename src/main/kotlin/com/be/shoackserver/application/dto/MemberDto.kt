package com.be.shoackserver.application.dto

import com.be.shoackserver.domain.entity.Member


class MemberDto {

    var id: Long? = null
    var appleUserId: String? = null
    var name: String? = null
    var imageName: String? = null

    companion object {
        fun of (member : Member) : MemberDto {
            val memberDto = MemberDto()
            memberDto.id = member.id
            memberDto.appleUserId = member.appleUserId
            memberDto.name = member.name
            memberDto.imageName = member.imageName
            return memberDto
        }
    }
}
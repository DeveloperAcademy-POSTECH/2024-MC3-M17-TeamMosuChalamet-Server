package com.be.shoackserver.application.dto

import com.be.shoackserver.domain.entity.Member


class MemberDto {
    var id: Long? = null
    var appleUserId: String? = null
    var name: String? = null
    var imageName: String? = null
    var deviceToken: String? = null
    var appleRefreshToken: String? = null
    var userAgent: String? = null
    var role: String? = null

    companion object {
        fun of (member : Member) : MemberDto {
            val memberDto = MemberDto()
            memberDto.id = member.id
            memberDto.appleUserId = member.appleUserId
            memberDto.name = member.name
            memberDto.imageName = member.imageName
            memberDto.deviceToken = member.deviceToken
            memberDto.appleRefreshToken = member.appleRefreshToken
            memberDto.userAgent = member.userAgent
            memberDto.role = member.role
            return memberDto
        }
    }

    constructor()

    constructor(
        id: Long?,
        appleUserId: String?,
        name: String?,
        imageName: String?,
        deviceToken: String?,
        appleRefreshToken: String?,
        userAgent: String?,
        role: String?
    ) {
        this.id = id
        this.appleUserId = appleUserId
        this.name = name
        this.imageName = imageName
        this.deviceToken = deviceToken
        this.appleRefreshToken = appleRefreshToken
        this.userAgent = userAgent
        this.role = role
    }
}
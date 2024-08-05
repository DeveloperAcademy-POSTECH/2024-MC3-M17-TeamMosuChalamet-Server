package com.be.shoackserver.presentation.response

import com.be.shoackserver.application.dto.MemberDto

class ProfileResponse {
    var id: Long? = null
    var name: String? = null
    var imageURL: String? = null

    companion object {
        fun of (memberDto : MemberDto) : ProfileResponse {
            val profileResponse = ProfileResponse()
            profileResponse.id = memberDto.id
            profileResponse.name = memberDto.name
            profileResponse.imageURL = memberDto.imageName
            return profileResponse
        }
    }
}


package com.be.shoackserver.presentation.response

import com.be.shoackserver.application.dto.MemberDto

class ProfileResponse {
    var name: String? = null
    var imageName: String? = null

    companion object {
        fun of (memberDto : MemberDto) : ProfileResponse {
            val profileResponse = ProfileResponse()
            profileResponse.name = memberDto.name
            profileResponse.imageName = memberDto.imageName
            return profileResponse
        }
    }
}


package com.be.shoackserver.presentation.response

import com.be.shoackserver.application.dto.MemberDto

class FriendResponse {

    var id: Long? = null
    var name : String? = null
    var imageName : String? = null

    companion object {
        fun of (memberDto: MemberDto) : FriendResponse {
            val friendResponse = FriendResponse()
            friendResponse.id = memberDto.id
            friendResponse.name = memberDto.name
            friendResponse.imageName = memberDto.imageName
            return friendResponse
        }
    }
}
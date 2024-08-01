package com.be.shoackserver.application.usecase

import com.be.shoackserver.application.dto.MemberDto
import com.be.shoackserver.application.service.AuthenticationService
import com.be.shoackserver.application.service.FriendService
import com.be.shoackserver.application.service.ImageService
import com.be.shoackserver.presentation.response.FriendResponse
import org.springframework.stereotype.Service

@Service
class FriendUseCase(
    private val friendService: FriendService,
    private val imageService: ImageService,
    private val authenticationService: AuthenticationService
) {
    private fun getMemberId() : Long {
        return authenticationService.getMemberIdFromSecurityContext()
    }

    fun allowFriendshipRequest(requesterId: Long) {
        friendService.saveFriendship(requesterId, getMemberId())
    }

    fun loadFriendList() : List<FriendResponse> {
        return friendService.getFriendList(getMemberId())
            .map { MemberDto.of(it) }
            .map { it.imageName = it.imageName?.let { imageService.generateS3URL("profile", it) }; it }
            .map { FriendResponse.of(it) }
    }
}
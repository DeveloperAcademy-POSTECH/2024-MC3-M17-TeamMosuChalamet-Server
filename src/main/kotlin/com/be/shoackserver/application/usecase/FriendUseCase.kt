package com.be.shoackserver.application.usecase

import com.be.shoackserver.application.dto.MemberDto
import com.be.shoackserver.application.service.FriendService
import com.be.shoackserver.application.service.ImageService
import com.be.shoackserver.presentation.response.FriendResponse
import org.springframework.stereotype.Service

@Service
class FriendUseCase(
    private val friendService: FriendService,
    private val imageService: ImageService
) {
    fun allowFriendshipRequest(requesterId: Long) {
        friendService.saveFriendship(requesterId)
    }

    fun loadFriendList() : List<FriendResponse> {
        return friendService.getFriendList()
            .map { MemberDto.of(it) }
            .map { it.imageName = it.imageName?.let { imageService.generateS3URL("profile", it) } ?: throw IllegalStateException("Image name is null"); it }
            .map { FriendResponse.of(it) }
    }
}
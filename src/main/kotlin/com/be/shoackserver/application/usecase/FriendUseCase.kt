package com.be.shoackserver.application.usecase

import com.be.shoackserver.application.dto.MemberDto
import com.be.shoackserver.application.service.AuthenticationService
import com.be.shoackserver.application.service.FriendService
import com.be.shoackserver.application.service.ImageService
import com.be.shoackserver.application.service.MemberService
import com.be.shoackserver.presentation.response.FriendResponse
import org.springframework.stereotype.Service

@Service
class FriendUseCase(
    private val memberService: MemberService,
    private val friendService: FriendService,
    private val imageService: ImageService,
    private val authenticationService: AuthenticationService
) {
    private fun getMemberId() : Long {
        return authenticationService.getMemberIdFromSecurityContext()
    }

    fun allowFriendshipRequest(requesterId: Long) {
        checkFriendNumberIsNotOver(requesterId)
        friendService.saveFriendship(requesterId, getMemberId())
    }

    fun loadFriendList() : List<FriendResponse> {
        return friendService.getFriendList(getMemberId())
            .map { MemberDto.of(it) }
            .map { it.imageName = it.imageName?.let { imageService.generateS3URL("profile", it) }; it }
            .map { FriendResponse.of(it) }
    }

    fun deleteFriend(friendId: Long) {
        friendService.deleteFriendShip(friendId, getMemberId())
    }

    private fun checkFriendNumberIsNotOver(requesterId: Long) {
        val member = memberService.findMemberById(getMemberId())
        val friend = memberService.findMemberById(requesterId)
        // 자신의 친구 수가 20명 이상일 경우 예외처리
        if ((member.friendshipsAsSubject.size + member.friendshipsAsObject.size) >= 20) {
            throw IllegalArgumentException("Member's friend number is over or equal to 20")
        }
        // 친구 신청자의 친구 수가 20명 이상일 경우 예외처리
        if ((friend.friendshipsAsSubject.size + friend.friendshipsAsObject.size) >= 20) {
            throw IllegalArgumentException("Requester's friend number is over or equal to 20")
        }
    }
}
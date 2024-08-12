package com.be.shoackserver.application.service

import com.be.shoackserver.exception.MemberNotFoundException
import com.be.shoackserver.domain.entity.Friendship
import com.be.shoackserver.domain.entity.Member
import com.be.shoackserver.domain.repository.FriendshipRepository
import com.be.shoackserver.domain.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class FriendService (
    private val memberRepository: MemberRepository,
    private val friendshipRepository: FriendshipRepository
) {
    fun saveFriendship(requesterId: Long, memberId: Long) {
        // 쟈기 자신인 경우 예외처리
        if (requesterId == memberId) {
            throw IllegalArgumentException("Cannot be friends with yourself")
        }

        val selfMember = memberRepository.findById(memberId)
            .orElseThrow() { MemberNotFoundException(memberId) }
        val requester = memberRepository.findById(requesterId)
            .orElseThrow() { MemberNotFoundException(requesterId) }

        // 이미 친구인 경우 예외처리
        if (friendshipRepository.existsBySubjectMemberAndObjectMember(selfMember, requester) ||
            friendshipRepository.existsBySubjectMemberAndObjectMember(requester, selfMember)
        ) {
            throw IllegalStateException("Already friends")
        }

        friendshipRepository.save(Friendship.create(requester, selfMember))
    }

    fun getFriendList(memberId: Long): List<Member> {
        val selfMember = memberRepository.findById(memberId)
            .orElseThrow() { MemberNotFoundException(memberId) }

        val friendListAsSubject = friendshipRepository
            .findAllBySubjectMember(selfMember)
            .filter { it.status == Friendship.FriendshipStatus.ACCEPTED }
            .map { it.objectMember }

        val friendListAsObject = friendshipRepository
            .findAllByObjectMember(selfMember)
            .filter { it.status == Friendship.FriendshipStatus.ACCEPTED }
            .map { it.subjectMember }

        return friendListAsSubject + friendListAsObject
    }

    fun deleteFriendShip(friendId: Long, memberId: Long) {
        val selfMember = memberRepository.findById(memberId)
            .orElseThrow() { MemberNotFoundException(memberId) }
        val friendMember = memberRepository.findById(friendId)
            .orElseThrow() { MemberNotFoundException(friendId) }

        friendshipRepository.findBySubjectMemberAndObjectMember(selfMember, friendMember)
            ?.let(friendshipRepository::delete)
            ?: friendshipRepository.findBySubjectMemberAndObjectMember(friendMember, selfMember)
                ?.let(friendshipRepository::delete)
                ?: throw IllegalStateException("There no friendship between you and the friend")
    }

}






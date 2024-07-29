package com.be.shoackserver.application.service

import com.be.shoackserver.application.dto.MemberDto
import com.be.shoackserver.application.exception.MemberNotFoundException
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
    private val memberId: Long = 1

    fun saveFriendship(requesterId: Long) {

        val selfMember = memberRepository.findById(memberId)
            .orElseThrow() { MemberNotFoundException(memberId) }
        val requester = memberRepository.findById(requesterId)
            .orElseThrow() {MemberNotFoundException(requesterId)}

            friendshipRepository.save(Friendship.create(requester, selfMember))
        }


    fun getFriendList(): List<Member> {

        val selfMember = memberRepository.findById(memberId)
            .orElseThrow() {MemberNotFoundException(memberId)}

        val friendListAsSubject = friendshipRepository
            .findAllBySubjectMember(selfMember)
            .filter { it.status == Friendship.FriendshipStatus.ACCEPTED }
            .mapNotNull { it.objectMember }

        val friendListAsObject = friendshipRepository
            .findAllByObjectMember(selfMember)
            .filter { it.status == Friendship.FriendshipStatus.ACCEPTED }
            .mapNotNull { it.subjectMember }

        return friendListAsSubject + friendListAsObject

    }

}






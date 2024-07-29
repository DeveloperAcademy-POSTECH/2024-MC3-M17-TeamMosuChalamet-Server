package com.be.shoackserver.domain.repository

import com.be.shoackserver.domain.entity.Friendship
import com.be.shoackserver.domain.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface FriendshipRepository : JpaRepository<Friendship, Long> {
    fun findAllBySubjectMember(member: Member): List<Friendship>
    fun findAllByObjectMember(member: Member): List<Friendship>

}
package com.be.shoackserver.domain.repository

import com.be.shoackserver.domain.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : JpaRepository<Member, Long>{
    fun findMemberByAppleUserId(appleUserId: String): Member?
}
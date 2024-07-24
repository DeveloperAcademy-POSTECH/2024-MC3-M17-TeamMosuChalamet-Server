package com.be.shoackserver.domain.repository

import com.be.shoackserver.domain.entity.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long>{
}
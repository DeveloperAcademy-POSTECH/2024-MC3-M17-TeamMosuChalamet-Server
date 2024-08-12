package com.be.shoackserver.domain.repository

import com.be.shoackserver.domain.entity.RefreshEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RefreshRepository : JpaRepository<RefreshEntity, Long> {
    fun findByRefreshToken(refreshToken: String): RefreshEntity?
    fun deleteByRefreshToken(refreshToken: String)
    fun deleteByMemberId(memberId: Long)
}
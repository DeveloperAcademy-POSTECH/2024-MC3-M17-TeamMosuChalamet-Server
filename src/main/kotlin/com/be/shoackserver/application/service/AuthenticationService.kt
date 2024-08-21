package com.be.shoackserver.application.service

import com.be.shoackserver.domain.entity.RefreshEntity
import com.be.shoackserver.domain.repository.RefreshRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthenticationService(
    private val refreshRepository: RefreshRepository,
) {
    fun getMemberIdFromSecurityContext() : Long {
        return SecurityContextHolder.getContext().authentication.name.toLong()
    }

    fun addRefreshEntity (refreshToken: String, memberId: Long, expiredMs: Long) {
        val expiredAt : Date = Date(System.currentTimeMillis() + expiredMs)

        val refreshEntity = RefreshEntity()
        refreshEntity.memberId = memberId
        refreshEntity.refreshToken = refreshToken
        refreshEntity.expiredAt = expiredAt.toString()

        refreshRepository.save(refreshEntity)
    }

    fun deleteRefreshEntity(refreshToken: String) {
        refreshRepository.deleteByRefreshToken(refreshToken)
    }
}
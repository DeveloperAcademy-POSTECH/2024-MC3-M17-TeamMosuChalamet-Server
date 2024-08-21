package com.be.shoackserver.presentation.controller

import com.be.shoackserver.application.service.AuthenticationService
import com.be.shoackserver.application.usecase.MemberManageUseCase
import com.be.shoackserver.domain.repository.RefreshRepository
import com.be.shoackserver.jwt.JWTUtil
import io.jsonwebtoken.JwtException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class JWTController(
    private val jwtUtil: JWTUtil,
    private val refreshRepository: RefreshRepository,
    private val memberManageUseCase: MemberManageUseCase
) {

    private val ACCESSTOKEN_EXPIRED_MS = 24 * 60 * 60 * 1000L // 1일
    private val REFRESHTOKEN_EXPIRED_MS = 30 * 24 * 60 * 60 * 1000L // 30일

    @PostMapping("/reissue")
    fun reissue(request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<String> {
        val refreshHeader = request.getHeader("Refresh")
        when {
            refreshHeader == null || !refreshHeader.startsWith("Bearer ") -> {
                throw IllegalArgumentException("Invalid refresh token")
            }

            else -> {
                // Bearer 다음에 있는 토큰 추출
                val refreshToken = refreshHeader.split(" ")[1]
                // 토큰 유효기간 검증
                try {
                    jwtUtil.isExpired(refreshToken)
                } catch (e: Exception) {
                    throw JwtException("Refresh token is expired")
                }

                val category = jwtUtil.getCategory(refreshToken)

                // 카테고리 검증
                if (category != "refresh") {
                    throw IllegalArgumentException("Invalid refresh token")
                }

                if(refreshRepository.findByRefreshToken(refreshToken) == null) {
                    throw JwtException("Invalid refresh token")
                }

                refreshRepository.deleteByRefreshToken(refreshToken)

                val memberId = jwtUtil.getMemberId(refreshToken)
                val role = jwtUtil.getRole(refreshToken)

                val newAccessToken = jwtUtil.generateToken("access", memberId, role, ACCESSTOKEN_EXPIRED_MS)
                val newRefreshToken = jwtUtil.generateToken("refresh", memberId, role, REFRESHTOKEN_EXPIRED_MS)

                memberManageUseCase.addRefreshToken(newRefreshToken, memberId, REFRESHTOKEN_EXPIRED_MS)

                response.setHeader("Access", "Bearer $newAccessToken")
                response.setHeader("Refresh", "Bearer $newRefreshToken")

                return ResponseEntity. ok().build()
            }
        }

    }






}
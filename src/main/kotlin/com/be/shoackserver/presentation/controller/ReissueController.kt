package com.be.shoackserver.presentation.controller

import com.be.shoackserver.jwt.JWTUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.extern.log4j.Log4j2
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Log4j2
@RestController
@RequestMapping("/api")
class ReissueController(
    private final val jwtUtil: JWTUtil
) {

    @PostMapping("/reissue")
    fun reissue(request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<String> {
        val refreshHeader = request.getHeader("Refresh")
        when {
            refreshHeader == null || !refreshHeader.startsWith("Bearer ") -> {
                println("token null")
                return ResponseEntity.badRequest().body("token null")
            }

            else -> {
                // Bearer 다음에 있는 토큰 추출
                val refreshToken = refreshHeader.split(" ")[1]

                // 토큰 유효기간 검증
                try {
                    jwtUtil.isExpired(refreshToken)
                } catch (e: Exception) {
                    println("token expired")
                    return ResponseEntity.badRequest().body("token expired")
                }

                val category = jwtUtil.getCategory(refreshToken)

                // 카테고리 검증
                if (category != "refresh") {
                    return ResponseEntity.badRequest().body("Invalid refresh token")
                }

                val username = jwtUtil.getUsername(refreshToken)
                val role = jwtUtil.getRole(refreshToken)

                val newAccessToken = jwtUtil.generateToken("access", username, role, 60 * 60 * 10 * 1000L) // 10시간
                val newRefreshToken = jwtUtil.generateToken("refresh", username, role, 60 * 24 * 60 * 60 * 1000L) // 60일

                // 헤더에 토큰을 실어 보내기
                response.setHeader("Access", "Bearer $newAccessToken")
                response.setHeader("Refresh", "Bearer $newRefreshToken")

                return ResponseEntity.ok().build()
            }
        }

    }
}
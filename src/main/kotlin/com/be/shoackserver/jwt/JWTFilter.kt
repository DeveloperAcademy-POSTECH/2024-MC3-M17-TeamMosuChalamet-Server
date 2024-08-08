package com.be.shoackserver.jwt

import com.be.shoackserver.application.dto.CustomUserDetails
import com.be.shoackserver.domain.entity.Member
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter


class JWTFilter(private val jwtUtil: JWTUtil) : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val authorizationHeader = request.getHeader("Access")

        when {
            authorizationHeader == null || !authorizationHeader.startsWith("Bearer ") -> {
                filterChain.doFilter(request, response)
                return
            }
            else -> {
                val accessToken = authorizationHeader.split(" ")[1]
                if (jwtUtil.isExpired(accessToken)) {
                    throw JwtException("Token is expired")
                }

                val category = jwtUtil.getCategory(accessToken)

                if(category != "access") {
                    throw JwtException("Invalid access token category")
                }

                val memberId = jwtUtil.getMemberId(accessToken)
                val role = jwtUtil.getRole(accessToken)

                val member = Member().apply {
                    this.id = memberId
                    this.role = role
                }

                val customUserDetails = CustomUserDetails(member)

                val authToken: Authentication = UsernamePasswordAuthenticationToken(
                    customUserDetails, "a", customUserDetails.authorities
                )

                SecurityContextHolder.getContext().authentication = authToken
                filterChain.doFilter(request, response)
            }
        }
    }
}

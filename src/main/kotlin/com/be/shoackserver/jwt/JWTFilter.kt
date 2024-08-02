package com.be.shoackserver.jwt

import com.be.shoackserver.application.dto.CustomUserDetails
import com.be.shoackserver.domain.entity.Member
import com.be.shoackserver.domain.repository.MemberRepository
import com.be.shoackserver.exception.UnauthorizedException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.extern.log4j.Log4j2
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.io.PrintWriter

@Log4j2
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
                    throw UnauthorizedException("Token is expired")
                }

                val category = jwtUtil.getCategory(accessToken)

                if(category != "access") {
                    throw UnauthorizedException("Invalid access token category")
                }

                val username = jwtUtil.getUsername(accessToken)
                val role = jwtUtil.getRole(accessToken)

                val member = Member().apply {
                    this.id = username.toLong()
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

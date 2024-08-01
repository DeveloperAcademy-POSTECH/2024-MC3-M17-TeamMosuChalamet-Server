package com.be.shoackserver.jwt

import com.be.shoackserver.application.dto.CustomUserDetails
import com.be.shoackserver.domain.entity.Member
import com.be.shoackserver.domain.repository.MemberRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.io.PrintWriter

class JWTFilter(private val jwtUtil: JWTUtil) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val authorizationHeader = request.getHeader("Access")

        when {
            authorizationHeader == null || !authorizationHeader.startsWith("Bearer ") -> {
                println("token null")
                filterChain.doFilter(request, response)
                return
            }
            else -> {
                val accessToken = authorizationHeader.split(" ")[1]
                println("token: $accessToken")
                if (jwtUtil.isExpired(accessToken)) {
                    val writer: PrintWriter = response.writer
                    writer.write("token expired")
                    println("token expired")

                    response.status = 401
                    return
                }

                val category = jwtUtil.getCategory(accessToken)

                if(category != "access") {

                    val writer: PrintWriter = response.writer
                    writer.write("invalid access token")
                    println("invalid access token")
                    return
                }

                val username = jwtUtil.getUsername(accessToken)
                val role = jwtUtil.getRole(accessToken)

                println("username: $username")
                println("role: $role")

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

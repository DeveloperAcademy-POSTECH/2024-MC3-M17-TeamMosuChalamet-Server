package com.be.shoackserver.jwt

import com.be.shoackserver.application.dto.CustomUserDetails
import com.be.shoackserver.application.usecase.LoginUseCase
import com.be.shoackserver.application.usecase.MemberManageUseCase
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.extern.log4j.Log4j2
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Log4j2
class LoginFilter(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JWTUtil,
    private val loginUseCase: LoginUseCase,
    private val memberManageUseCase: MemberManageUseCase
) : UsernamePasswordAuthenticationFilter() {
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val identityToken = request.getHeader("Identity-Token").split(" ")[1]

        // 애플 로그인 등장!
        val appleUserId = loginUseCase.signIn(identityToken)
        // 애플 고유 아이디를 세션에 담아두고 싶진 않다..
        val authenticationToken = UsernamePasswordAuthenticationToken(appleUserId, "uotp", null)

        return authenticationManager.authenticate(authenticationToken)
    }

    override fun successfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain, authResult: Authentication) {
        val authorizationCode = request.getHeader("Authorization-Code") ?: throw IllegalArgumentException("Authorization-Code is null")

        val customUserDetails = authResult.principal as CustomUserDetails
        val memberId = customUserDetails.username.toLong()
        val role = customUserDetails.authorities.first().authority

        // refresh token 저장
        memberManageUseCase.saveAppleRefreshToken(memberId, authorizationCode)

        // jwt 생성
        val accessToken = jwtUtil.generateToken("access", memberId, role, 30 * 24 * 60 * 60 * 1000L) // 30일
        val refreshToken = jwtUtil.generateToken("refresh", memberId, role, 60 * 24 * 60 * 60 * 1000L) // 60일

        response.addHeader("Access", "Bearer $accessToken")
        response.addHeader("Refresh", "Bearer $refreshToken")

        response.status = 200
        response.contentType = "application/json"

        println("success")
    }

    override fun unsuccessfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, failed: AuthenticationException) {
        // 회원정보 저장로직
        val identityTokenHeader = request.getHeader("Identity-Token") ?: throw IllegalArgumentException("Identity-Token is null")
        val identityToken = identityTokenHeader.split(" ")[1]
        val deviceToken = request.getHeader("Device-Token") ?: throw IllegalArgumentException("Device-Token is null")
        val authorizationCode = request.getHeader("Authorization-Code") ?: throw IllegalArgumentException("Authorization-Code is null")

        // 애플 인증으로 appleUserId를 받아온다
        val appleUserId = loginUseCase.signIn(identityToken)

        val memberDto = memberManageUseCase.addNewMember(appleUserId, "이름 없음", deviceToken)

        // refresh token 저장
        memberManageUseCase.saveAppleRefreshToken(memberDto.id ?: throw IllegalArgumentException("Member id is null"), authorizationCode)

        // jwt 생성
        val accessToken = jwtUtil.generateToken("access", memberDto.id!!, memberDto.role!!,  24 * 60 * 60 * 1000L) // 1일
        val refreshToken = jwtUtil.generateToken("refresh", memberDto.id!!, memberDto.role!!, 30 * 24 * 60 * 60 * 1000L) // 30일

        response.addHeader("Access", "Bearer $accessToken")
        response.addHeader("Refresh", "Bearer $refreshToken")

        response.status = 201
    }
}


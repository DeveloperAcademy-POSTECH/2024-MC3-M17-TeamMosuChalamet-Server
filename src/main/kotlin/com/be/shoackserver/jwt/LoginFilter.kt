package com.be.shoackserver.jwt

import com.be.shoackserver.application.dto.CustomUserDetails
import com.be.shoackserver.application.service.MemberService
import com.be.shoackserver.application.usecase.LoginUseCase
import com.be.shoackserver.application.usecase.MemberManageUseCase
import com.be.shoackserver.domain.entity.RefreshEntity
import com.be.shoackserver.domain.repository.RefreshRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.extern.log4j.Log4j2
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.util.*

@Log4j2
class LoginFilter(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JWTUtil,
    private val loginUseCase: LoginUseCase,
    private val memberManageUseCase: MemberManageUseCase,
    private val memberService: MemberService
) : UsernamePasswordAuthenticationFilter() {

    private val ACCESSTOKEN_EXPIRED_MS = 24 * 60 * 60 * 1000L // 1일
    private val REFRESHTOKEN_EXPIRED_MS = 30 * 24 * 60 * 60 * 1000L // 30일

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val identityToken = request.getHeader("Identity-Token").split(" ")[1]

        val userAgentHeader = request.getHeader("User-Agent") ?: throw IllegalArgumentException("User-Agent is null")
        val userAgent = userAgentHeader.contains("AppClip").let { if (it) "appClip" else "app" }

        // 애플 로그인 등장!
        val appleUserId = loginUseCase.signIn(identityToken, userAgent)
        // 애플 고유 아이디를 세션에 담아두고 싶진 않다..
        val authenticationToken = UsernamePasswordAuthenticationToken(appleUserId, "uotp", null)

        return authenticationManager.authenticate(authenticationToken)
    }

    override fun successfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain, authResult: Authentication) {
        val authorizationCode = request.getHeader("Authorization-Code") ?: throw IllegalArgumentException("Authorization-Code is null")
        val deviceToken = request.getHeader("Device-Token") ?: throw IllegalArgumentException("Device-Token is null")
        val userAgentHeader = request.getHeader("User-Agent") ?: throw IllegalArgumentException("User-Agent is null")
        val userAgent = userAgentHeader.contains("AppClip").let { if (it) "appClip" else "app" }

        val customUserDetails = authResult.principal as CustomUserDetails
        val memberId = customUserDetails.username.toLong()
        val role = customUserDetails.authorities.first().authority

        // device token 저장
        memberService.updateMemberDeviceToken(memberId, deviceToken)

        // user agent 저장
        memberManageUseCase.saveUserAgent(memberId, userAgent)

        // refresh token 저장
        memberManageUseCase.saveAppleRefreshToken(memberId, authorizationCode, userAgent)

        // jwt 생성
        val accessToken = jwtUtil.generateToken("access", memberId, role, ACCESSTOKEN_EXPIRED_MS)
        val refreshToken = jwtUtil.generateToken("refresh", memberId, role, REFRESHTOKEN_EXPIRED_MS)

        memberManageUseCase.addRefreshToken(refreshToken, memberId, REFRESHTOKEN_EXPIRED_MS) // refresh token 저장

        response.addHeader("Access", "Bearer $accessToken")
        response.addHeader("Refresh", "Bearer $refreshToken")

        response.status = 200
        response.contentType = "application/json"
    }

    override fun unsuccessfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, failed: AuthenticationException) {
        // 회원정보 저장로직
        val identityTokenHeader = request.getHeader("Identity-Token") ?: throw IllegalArgumentException("Identity-Token is null")
        val identityToken = identityTokenHeader.split(" ")[1]
        val deviceToken = request.getHeader("Device-Token") ?: throw IllegalArgumentException("Device-Token is null")
        val authorizationCode = request.getHeader("Authorization-Code") ?: throw IllegalArgumentException("Authorization-Code is null")

        // 메서드로 만들기
        val userAgentHeader = request.getHeader("User-Agent") ?: throw IllegalArgumentException("User-Agent is null")
        val userAgent = userAgentHeader.contains("AppClip").let { if (it) "appClip" else "app" }

        // 애플 인증으로 appleUserId를 받아온다
        val appleUserId = loginUseCase.signIn(identityToken, userAgent)

        val memberDto = memberManageUseCase.addNewMember(appleUserId, "이름을 입력해주세요", deviceToken)

        // user agent 저장
        memberManageUseCase.saveUserAgent(memberDto.id ?: throw IllegalArgumentException("Member id is null"), userAgent)

        // refresh token 저장
        memberManageUseCase.saveAppleRefreshToken(memberDto.id ?: throw IllegalArgumentException("Member id is null"), authorizationCode, userAgent)

        // jwt 생성
        val accessToken = jwtUtil.generateToken("access", memberDto.id!!, memberDto.role!!,  ACCESSTOKEN_EXPIRED_MS) // 1일
        val refreshToken = jwtUtil.generateToken("refresh", memberDto.id!!, memberDto.role!!, REFRESHTOKEN_EXPIRED_MS) // 30일

        memberManageUseCase.addRefreshToken(refreshToken, memberDto.id!!, REFRESHTOKEN_EXPIRED_MS) // refresh token 저장

        response.addHeader("Access", "Bearer $accessToken")
        response.addHeader("Refresh", "Bearer $refreshToken")

        response.status = 201
    }

}


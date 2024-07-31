package com.be.shoackserver.jwt

import com.be.shoackserver.application.dto.CustomUserDetails
import com.be.shoackserver.application.usecase.LoginUseCase
import com.be.shoackserver.application.usecase.MemberManageUseCase
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class LoginFilter(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JWTUtil,
    private val loginUseCase: LoginUseCase,
    private val memberManageUseCase: MemberManageUseCase
) : UsernamePasswordAuthenticationFilter() {

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val identityToken = request.getParameter("identityToken")//obtainUsername(request)
        val name = request.getParameter("name")//obtainPassword(request)
        val deviceToken = request.getParameter("deviceToken")

        // 애플 로그인 등장!
        val appleUserId = loginUseCase.signIn(identityToken)
        // 애플 고유 아이디를 세션에 담아두고 싶진 않다..

        val authenticationToken = UsernamePasswordAuthenticationToken(appleUserId, name, null)

        return authenticationManager.authenticate(authenticationToken)
    }

    override fun successfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain, authResult: Authentication) {
        val customUserDetails = authResult.principal as CustomUserDetails
        val name = customUserDetails.username
        val role = customUserDetails.authorities.first().authority

        // jwt 생성
        val accessToken = jwtUtil.generateToken("access", name, role, 30 * 24 * 60 * 60 * 1000L) // 30일
        val refreshToken = jwtUtil.generateToken("refresh", name, role, 60 * 24 * 60 * 60 * 1000L) // 60일

        response.addHeader("Access", "Bearer $accessToken")
        response.addHeader("Refresh", "Bearer $refreshToken")

        response.status = 200
        response.contentType = "application/json"

        println("success")
    }

    override fun unsuccessfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, failed: AuthenticationException) {
        // 회원정보 저장로직
        val identityToken = request.getParameter("identityToken")
        val name = request.getParameter("name")
        val deviceToken = request.getParameter("deviceToken")

        val appleUserId = loginUseCase.signIn(identityToken)

        val memberDto = memberManageUseCase.addNewMember(appleUserId, name, deviceToken)

        // jwt 생성
        val accessToken = jwtUtil.generateToken("access", memberDto.id.toString(), memberDto.role!!, 30 * 24 * 60 * 60 * 1000L) // 30일
        val refreshToken = jwtUtil.generateToken("refresh", memberDto.name!!, memberDto.role!!, 60 * 24 * 60 * 60 * 1000L) // 60일

        response.addHeader("Access", "Bearer $accessToken")
        response.addHeader("Refresh", "Bearer $refreshToken")
        //

        response.status = 201
    }
}


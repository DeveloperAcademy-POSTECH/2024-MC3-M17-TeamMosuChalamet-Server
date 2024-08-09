package com.be.shoackserver.application.usecase

import com.be.shoackserver.appleSignin.*
import com.be.shoackserver.application.service.AppleAuthService
import com.be.shoackserver.application.service.AuthenticationService
import com.be.shoackserver.application.service.MemberService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class LoginUseCase(
    private val appleOAuthUserProvider: AppleOAuthUserProvider,
    private val memberService: MemberService,
    private val authenticationService: AuthenticationService
) {

    private fun getMemberId() : Long {
        return authenticationService.getMemberIdFromSecurityContext()
    }

    fun signIn(identityToken: String, userAgent: String) : String{
        return appleOAuthUserProvider.getAppleOAuthUser(identityToken, userAgent)
    }

    fun signOut(request: HttpServletRequest) {
        // refreshToken 무효화 시키기
        val refreshToken = request.getHeader("Refresh") ?: throw IllegalArgumentException("Refresh Token is null")
        // 디바이스 토큰 DB에서 삭제
        memberService.deleteDeviceToken(getMemberId())
    }
}
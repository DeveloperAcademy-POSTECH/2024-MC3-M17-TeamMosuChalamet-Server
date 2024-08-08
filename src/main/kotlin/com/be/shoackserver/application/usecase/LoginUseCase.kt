package com.be.shoackserver.application.usecase

import com.be.shoackserver.appleSignin.*
import com.be.shoackserver.application.service.AppleAuthService
import com.be.shoackserver.application.service.AuthenticationService
import com.be.shoackserver.application.service.MemberService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class LoginUseCase(
    private val appleOAuthUserProvider: AppleOAuthUserProvider
) {
    fun signIn(identityToken: String) : String{
        return appleOAuthUserProvider.getAppleOAuthUser(identityToken)
    }
}
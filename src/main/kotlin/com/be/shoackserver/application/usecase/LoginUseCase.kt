package com.be.shoackserver.application.usecase

import com.be.shoackserver.appleSignin.AppleOAuthUserProvider
import org.springframework.stereotype.Service

@Service
class LoginUseCase(
    private val appleOAuthUserProvider: AppleOAuthUserProvider
) {

    fun signIn(identityToken: String) : String{
        return appleOAuthUserProvider.getAppleOAuthUser(identityToken)
    }
}
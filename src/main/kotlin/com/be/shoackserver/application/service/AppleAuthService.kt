package com.be.shoackserver.application.service

import com.be.shoackserver.appleSignin.*
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class AppleAuthService(
    @Value("\${oauth.apple.client-id}") private val clientId: String,
    private val clientSecretGenerator: ClientSecretGenerator,
    private val appleClient: AppleAuthClient
) {

    fun getAppleTokens(authorizationCode: String) : AppleTokenResponse {
        println("getAppleTokens start")
        val clientSecret = clientSecretGenerator.generate()
        val appleTokenRequest = AppleTokenRequest(clientId, clientSecret, authorizationCode, "authorization_code")
        val appleTokenRequestJson = Gson().toJson(appleTokenRequest)
        val appleTokenResponse = appleClient.getAppleTokens(appleTokenRequest)


        return appleTokenResponse.body ?: throw Exception("Failed to get apple tokens")
    }

    fun requestToRevokeAppleToken(refreshToken: String) {
        val clientSecret = clientSecretGenerator.generate()
        try {
            appleClient.revokeAppleToken(
                AppleSignInRevokeRequest(
                    clientId,
                    clientSecret,
                    refreshToken,
                    "refresh_token"
                )
            ) // Apple 서버에 회원 탈퇴 요청
        } catch (e: Exception) {
            // Apple 서버에 회원 탈퇴 요청 실패 시
            throw Exception("Failed to revoke apple token")
        }
    }
}
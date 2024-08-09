package com.be.shoackserver.application.service

import com.be.shoackserver.appleSignin.*
import com.google.gson.Gson
import org.eclipse.jgit.transport.UserAgent
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class AppleAuthService(
    @Value("\${oauth.apple.client-id}") private val clientId: String,
    @Value("\${oauth.apple.appClip-client-id}") private val appClipClientId: String,
    private val clientSecretGenerator: ClientSecretGenerator,
    private val appleClient: AppleAuthClient
) {

    fun getAppleTokens(authorizationCode: String, userAgent: String) : AppleTokenResponse {

        when (userAgent) {
            "appClip" -> {
                val clientSecret = clientSecretGenerator.generate(appClipClientId)
                val appleTokenRequest = AppleTokenRequest(appClipClientId, clientSecret, authorizationCode, "authorization_code")
                val appleTokenResponse = appleClient.getAppleTokens(appleTokenRequest)
                return appleTokenResponse.body ?: throw RuntimeException("Failed to get apple tokens")
            }
            else -> {
                val clientSecret = clientSecretGenerator.generate(clientId)
                val appleTokenRequest = AppleTokenRequest(clientId, clientSecret, authorizationCode, "authorization_code")
                val appleTokenResponse = appleClient.getAppleTokens(appleTokenRequest)
                return appleTokenResponse.body ?: throw RuntimeException("Failed to get apple tokens")

            }
        }
    }

    fun requestToRevokeAppleToken(refreshToken: String, userAgent: String) {

        when (userAgent) {
            "appClip" -> {
                try {
                    val clientSecret = clientSecretGenerator.generate(appClipClientId)
                    appleClient.revokeAppleToken(
                        AppleSignInRevokeRequest(
                            appClipClientId,
                            clientSecret,
                            refreshToken,
                            "refresh_token"
                        )
                    ) // Apple 서버에 회원 탈퇴 요청
                } catch (e: Exception) {
                    // Apple 서버에 회원 탈퇴 요청 실패 시
                    throw RuntimeException("Failed to revoke apple token")
                }
            }
            else -> {
                try {
                    val clientSecret = clientSecretGenerator.generate(clientId)
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
                    throw RuntimeException("Failed to revoke apple token")
                }
            }
        }


    }
}
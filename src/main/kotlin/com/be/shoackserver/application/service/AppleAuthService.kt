package com.be.shoackserver.application.service

import com.be.shoackserver.appleSignin.*
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class AppleAuthService(
    @Value("\${oauth.apple.client-id}") private val clientId: String,
    @Value("\${oauth.apple.appClip-client-id}") private val appClipClientId: String,
    private val clientSecretGenerator: ClientSecretGenerator,
    private val appleClient: AppleAuthClient
) {
    private val log = LogManager.getLogger(AppleAuthService::class.java)

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
        log.info("Attempting to revoke Apple token")

        when (userAgent) {
            "appClip" -> {
                try {
                    val clientSecret = clientSecretGenerator.generate(appClipClientId)
                    log.info("finished to generate client secret for appClip")
                    val response = appleClient.revokeAppleToken(AppleRevokeRequest(
                        appClipClientId,
                        clientSecret,
                        refreshToken,
                        "refresh_token"
                    )) // Apple 서버에 회원 탈퇴 요청
                } catch (e: Exception) {
                    // Apple 서버에 회원 탈퇴 요청 실패 시
                    throw RuntimeException(e.message)
                }
            }
            else -> {
                try {
                    val clientSecret = clientSecretGenerator.generate(clientId)
                    log.info("finished to generate client secret for app")
                    val response = appleClient.revokeAppleToken(AppleRevokeRequest(
                        clientId,
                        clientSecret,
                        refreshToken,
                        "refresh_token"
                    )) // Apple 서버에 회원 탈퇴 요청
                } catch (e: Exception) {
                    // Apple 서버에 회원 탈퇴 요청 실패 시
                    throw RuntimeException(e.message)
                }
            }
        }


    }
}
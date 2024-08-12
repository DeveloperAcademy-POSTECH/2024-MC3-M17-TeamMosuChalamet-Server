package com.be.shoackserver.appleSignin

import io.jsonwebtoken.Claims
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class AppleClaimsValidator(
    @Value("\${oauth.apple.endpoint}") private val iss: String, // 토큰 발행자: https://appleid.apple.com
    @Value("\${oauth.apple.client-id}") private val clientId: String, // 클라이언트 ID: 앱의 번들 ID
    @Value("\${oauth.apple.appClip-client-id}") private val appClipClientId: String, // 클라이언트 ID: 앱 클립의 번들 ID
    @Value("\${oauth.apple.nonce}") private val nonce: String, // 클라이언트에서 생성한 nonce
) {
    private val encryptedNonce: String = HashUtils.hash(nonce)

    fun validate(claims: Claims, userAgent: String) : Boolean{
        return when (userAgent) {
            "appClip" -> {
                claims.issuer.contains(iss) && // iss 검증
                        claims.audience.contains(appClipClientId) //&& // aud 검증
                //claims["nonce"].toString().contains(encryptedNonce) // nonce 검증
            }

            else -> {
                claims.issuer.contains(iss) && // iss 검증
                        claims.audience.contains(clientId) //&& // aud 검증
                //claims["nonce"].toString().contains(encryptedNonce) // nonce 검증
            }
        }
    }
}

/**
 * 아래의 애플 로그인 요구사항을 준수하기 위해서 검증이 필요하다
 * - Verify the nonce for the authentication
 *
 * - Verify that the iss field contains `https://appleid.apple.com`
 *
 * - Verify that the aud field is the developer’s client_id
 */
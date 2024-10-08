package com.be.shoackserver.appleSignin

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.security.PublicKey
import java.security.SignatureException
import java.util.*

@Component
class AppleJwtParser {
    companion object {
        private var objectMapper = jacksonObjectMapper()
    }

    fun parseHeaders(identityToken: String) : Map<String, String>  {
        try {
            val encodedHeader = identityToken.split(".")[0] // . 으로 헤더, 페이로드, 시그니처 분리하고 헤더만 뽑아오기
            val decodedHeader = String(Base64.getUrlDecoder().decode(encodedHeader)) // 헤더를 base64 디코딩하기
            return objectMapper.readValue(decodedHeader)
        } catch (e: Exception) {
            throw RuntimeException("Failed to parse headers from identity token")
        }
    }

    fun parsePublicKeyAndGetClaims(identityToken: String, publicKey: PublicKey) : Claims {
        return try {
            Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(identityToken)
                .payload

        } catch (e: SignatureException) {
            throw SignatureException("Invalid Apple identity token signature", e)
        } catch (e: ExpiredJwtException){
            throw ExpiredJwtException(null, null, "Apple identity token expired", e)
        } catch (e: Exception) {
            throw RuntimeException("Failed to parse identity token", e)
        }

    }
}
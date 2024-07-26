package com.be.shoackserver.appleSignin

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import org.bouncycastle.pqc.crypto.ExchangePair
import java.security.PublicKey
import java.security.SignatureException
import java.util.*

class AppleJwtParser {

    companion object {
        private var IDENTITY_TOKEN_VALUE_DELIMITER : String = "\\."
        private var IDENTITY_TOKEN_HEADER_INDEX = 0

        private var OBJECT_MAPPER = jacksonObjectMapper()

        fun parseHeaders(identityToken: String) : Map<String, String>  {
            try {
                val encodedHeader = identityToken.split(IDENTITY_TOKEN_VALUE_DELIMITER)[IDENTITY_TOKEN_HEADER_INDEX]
                val decodedHeader = String(Base64.getUrlDecoder().decode(encodedHeader))
                return OBJECT_MAPPER.readValue(decodedHeader)
            } catch (e: Exception) {
                throw RuntimeException("Failed to parse headers from identity token")
            }
        }

        fun parsePublicKeyAndGetClaims(identityToken: String, publicKey: PublicKey) : Claims {
            return try {
                Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(identityToken)
                    .body
            } catch (e: SignatureException) {
                throw SecurityException("Invalid token signature", e)
            } catch (e: ExpiredJwtException){
                throw SecurityException("Token expired", e)
            } catch (e: Exception) {
                throw IllegalArgumentException("Failed to parse identity token", e)
            }

        }
    }
}
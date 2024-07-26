package com.be.shoackserver.appleSignin

import org.springframework.stereotype.Component
import java.math.BigInteger
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.PublicKey
import java.security.spec.InvalidKeySpecException
import java.security.spec.RSAPublicKeySpec
import java.util.*

@Component
class PublicKeyGenerator {

    companion object {
        private val SIGN_ALGORITHM_HEADER_KEY = "alg"
        private val KEY_ID_HEADER_KEY = "kid"
        private val POSITIVE_SIGN_NUMBER = 1
    }

    fun generatePublicKey(headers : Map<String, String>, applePublicKeys: ApplePublicKeys) : PublicKey {
        var applePublicKey = applePublicKeys.getMatchesKey(headers[SIGN_ALGORITHM_HEADER_KEY], headers[KEY_ID_HEADER_KEY])

        return generatePublicKeyWithApplePublicKey(applePublicKey)
    }

    private fun generatePublicKeyWithApplePublicKey(applePublicKey: ApplePublicKey) : PublicKey {
        var nBytes = Base64.getUrlDecoder().decode(applePublicKey.n)
        var eBytes = Base64.getUrlDecoder().decode(applePublicKey.e)

        var n = BigInteger(POSITIVE_SIGN_NUMBER, nBytes)
        var e = BigInteger(POSITIVE_SIGN_NUMBER, eBytes)

        var publicKeySpec = RSAPublicKeySpec(n, e)

        try {
            var keyFactory = KeyFactory.getInstance(applePublicKey.kty)

            return keyFactory.generatePublic(publicKeySpec)
        } catch (e : Exception) {
            when (e) {
                is NoSuchAlgorithmException -> {
                    throw RuntimeException("Unsupported key algorithm: ${applePublicKey.kty}", e)
                }

                is InvalidKeySpecException -> {
                    throw RuntimeException("Invalid key specification", e)
                }

                else -> {
                    throw RuntimeException("Failed to generate public key", e)
                }
            }
        }
    }
}
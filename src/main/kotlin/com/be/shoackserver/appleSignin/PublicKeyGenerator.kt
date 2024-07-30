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

    // identity token의 alg와 kid와 일치하는 public key를 확인하기
    fun generatePublicKey(headers : Map<String, String>, applePublicKeys: ApplePublicKeys) : PublicKey {
        var applePublicKey = applePublicKeys.getMatchesKey(headers["alg"], headers["kid"])

        return generatePublicKeyWithApplePublicKey(applePublicKey)
    }

    private fun generatePublicKeyWithApplePublicKey(applePublicKey: ApplePublicKey) : PublicKey {
        var nBytes = Base64.getUrlDecoder().decode(applePublicKey.n) // 애플 public key의 n을 base64 디코딩 -> byte 배열
        var eBytes = Base64.getUrlDecoder().decode(applePublicKey.e) // 애플 public key의 e을 base64 디코딩 -> byte 배열

        var n = BigInteger(1, nBytes) // Base64 디코딩된 바이트 배열은 부호 없는 형태이기 때문에 BigInteger의 부호 없는 생성자를 사용
        var e = BigInteger(1, eBytes) // Base64 디코딩된 바이트 배열은 부호 없는 형태이기 때문에 BigInteger의 부호 없는 생성자를 사용

        var publicKeySpec = RSAPublicKeySpec(n, e)

        try {
            var keyFactory = KeyFactory.getInstance(applePublicKey.kty) // 애플 public key의 kty로 KeyFactory 생성

            return keyFactory.generatePublic(publicKeySpec) // KeyFactory를 사용하여 public key 사양으로 새로운 PublicKey 생성
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
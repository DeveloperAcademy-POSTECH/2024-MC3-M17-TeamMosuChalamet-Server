package com.be.shoackserver.appleSignin

import io.jsonwebtoken.Claims
import org.springframework.stereotype.Component
import java.security.PublicKey

@Component
class AppleOAuthUserProvider(

    private val appleClient: AppleClient,
    private val jwtParser: AppleJwtParser,
    private val publicKeyGenerator: PublicKeyGenerator,
    private val appleClaimsValidator: AppleClaimsValidator
) {

    fun getAppleOAuthUser(identityToken : String) : String{
        // 1. identity token 받아오기 -> 완료
        // 2. 애플 공개키 목록 받아오기
        appleClient.getApplePublicKeys()
        // 3. 애플 공개키 목록 중 identity token의 alg와 kid와 일치하는 public key를 가져오기
        jwtParser.parseHeaders(identityToken)
        // 4. 가져온 public key의 n, e를 사용해서 새로운 공개키를 만들기
        val publicKey : PublicKey = publicKeyGenerator.generatePublicKey(jwtParser.parseHeaders(identityToken), appleClient.getApplePublicKeys())
        // 5. 새로운 공개키로 identity token을 검증하고 payload 가져오기
        val claims : Claims =  jwtParser.parsePublicKeyAndGetClaims(identityToken, publicKey)
        // 6. payload의 클레임 검증하기
        if(!appleClaimsValidator.validate(claims)){
            throw RuntimeException("Apple claims validation failed")
        }

        val userId = claims.subject.toString() // 유저 아이디

        return userId
    }
}
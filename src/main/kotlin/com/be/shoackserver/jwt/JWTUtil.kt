package com.be.shoackserver.jwt

import io.jsonwebtoken.Jwts
import lombok.extern.log4j.Log4j2
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Log4j2
@Component
class JWTUtil(@Value("\${jwt.secret}") secret: String) {
    private val secretKey: SecretKey = SecretKeySpec(secret.toByteArray(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().algorithm)

    fun getMemberId(token: String): Long = Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .payload
        .get("memberId", String::class.java).toLong()

    fun getRole(token: String): String = Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .payload
        .get("role", String::class.java)

    fun getCategory(token: String): String = Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .payload
        .get("category", String::class.java)

    fun isExpired(token: String): Boolean = Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .payload
        .expiration
        .before(Date())

    /**
     * expiredMs = 토큰 유효시간
     */
    fun generateToken(category: String,  memberId: Long, role: String, expiredMs: Long): String = Jwts.builder()
        .claim("category", category)
        .claim("memberId", memberId.toString())
        .claim("role", role)
        .issuedAt(Date(System.currentTimeMillis()))
        .expiration(Date(System.currentTimeMillis() + expiredMs))
        .signWith(secretKey)
        .compact()
}


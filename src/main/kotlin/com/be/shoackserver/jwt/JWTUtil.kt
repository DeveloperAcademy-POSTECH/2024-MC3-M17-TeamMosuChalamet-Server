package com.be.shoackserver.jwt

import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Component
class JWTUtil(@Value("\${jwt.secret}") secret: String) {

    private val secretKey: SecretKey = SecretKeySpec(secret.toByteArray(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().algorithm)

    fun getUsername(token: String): String = Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .payload
        .get("username", String::class.java)

    fun getRole(token: String): String = Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .payload
        .get("role", String::class.java)

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
    fun generateToken(username: String, role: String, expiredMs: Long): String = Jwts.builder()
        .claim("username", username)
        .claim("role", role)
        .issuedAt(Date(System.currentTimeMillis()))
        .expiration(Date(System.currentTimeMillis() + expiredMs))
        .signWith(secretKey)
        .compact()
}

//package com.be.shoackserver.jwt;
//
//import io.jsonwebtoken.Jwts;
//import lombok.RequiredArgsConstructor;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//import java.nio.charset.StandardCharsets;
//import java.util.Date;
//
//@Component
//@RequiredArgsConstructor
//public class JWTUtil {
//
//    private SecretKey secretKey;
//
//    public JWTUtil(@Value("${jwt.secret}") String secret) {
//        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
//    }
//
//    public String getUsername(String token) {
//        return Jwts.parser()
//            .verifyWith(secretKey)
//            .build()
//            .parseSignedClaims(token)
//            .getPayload()
//            .get("username", String.class);
//    }
//
//    public String getRole(String token) {
//        return Jwts.parser()
//            .verifyWith(secretKey)
//            .build()
//            .parseSignedClaims(token)
//            .getPayload()
//            .get("role", String.class);
//    }
//
//    public boolean isExpired(String token) {
//        return Jwts.parser()
//            .verifyWith(secretKey)
//            .build()
//            .parseSignedClaims(token)
//            .getPayload()
//            .getExpiration()
//            .before(new Date());
//    }
//    /**
//     * expiredMs = 토큰 유효시간
//     * */
//    public String generateToken(String username, String role, Long expiredMs) {
//        return Jwts.builder()
//            .claim("username", username)
//            .claim("role", role)
//            .issuedAt(new Date(System.currentTimeMillis()))
//            .expiration(new Date(System.currentTimeMillis() + expiredMs))
//            .signWith(secretKey)
//            .compact();
//
//    }
//
//}

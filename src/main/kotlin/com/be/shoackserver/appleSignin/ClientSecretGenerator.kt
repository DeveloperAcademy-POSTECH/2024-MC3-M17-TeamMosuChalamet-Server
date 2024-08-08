package com.be.shoackserver.appleSignin

import io.jsonwebtoken.Jwt
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*

@Component
class ClientSecretGenerator(
    @Value("\${oauth.apple.key-id}") private val keyId: String,
    @Value("\${oauth.apple.team-id}") private val teamId: String,
    @Value("\${oauth.apple.client-id}") private val clientId: String,
    @Value("\${oauth.apple.endpoint}") private val endpoint: String,
    @Value("\${oauth.apple.p8-key-name}") private val keyFileName: String
) {
    private val SIX_MONTHS_IN_MILLISECONDS = 6L * 30 * 24 * 60 * 60 * 1000

    fun generate() = Jwts.builder()
        .header()
        .add("alg", "ES256")
        .add("kid", keyId)
        .and()
        .issuer(teamId)
        .issuedAt(Date(System.currentTimeMillis()))
        .expiration(Date(System.currentTimeMillis() + SIX_MONTHS_IN_MILLISECONDS))
        .audience().add(endpoint).and()
        .subject(clientId)
        .signWith(loadPrivateKey(keyFileName), Jwts.SIG.ES256)
        .compact()

    private fun loadPrivateKey(filename: String): PrivateKey {
        val resource = ClassPathResource(filename)
        val keyBytes = resource.inputStream.readAllBytes()
        val keySpec =  PKCS8EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance("EC")

        return keyFactory.generatePrivate(keySpec)

    }





}
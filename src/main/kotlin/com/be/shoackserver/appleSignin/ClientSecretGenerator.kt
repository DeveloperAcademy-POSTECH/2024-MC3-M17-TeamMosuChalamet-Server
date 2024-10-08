package com.be.shoackserver.appleSignin

import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*


@Component
class ClientSecretGenerator(
    @Value("\${oauth.apple.key-id}") private val keyId: String,
    @Value("\${oauth.apple.team-id}") private val teamId: String,
    @Value("\${oauth.apple.endpoint}") private val endpoint: String,
    @Value("\${oauth.apple.p8-key-name}") private val keyName: String,
    @Value("\${oauth.apple.p8-key}") private val privateKey: String
) {
    private val ONE_MONTH_IN_MILLISECONDS = 1L * 30 * 24 * 60 * 60 * 1000

    fun generate(clientId: String) : String = Jwts.builder()
        .header()
        .add("alg", "ES256")
        .add("kid", keyId)
        .and()
        .issuer(teamId)
        .issuedAt(Date(System.currentTimeMillis()))
        .expiration(Date(System.currentTimeMillis() + ONE_MONTH_IN_MILLISECONDS))
        .audience().add(endpoint).and()
        .subject(clientId)
        .signWith(loadPrivateKey(privateKey), Jwts.SIG.ES256)
        .compact()

    private fun loadPrivateKey(privateKey: String) : PrivateKey {
        val keyContent = Base64.getDecoder().decode(privateKey)
        val keySpec =  PKCS8EncodedKeySpec(keyContent)
        val keyFactory = KeyFactory.getInstance("EC")

        return keyFactory.generatePrivate(keySpec)
    }

}
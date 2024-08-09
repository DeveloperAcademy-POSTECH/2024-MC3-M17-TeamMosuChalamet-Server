package com.be.shoackserver.appleSignin

import io.jsonwebtoken.Jwts
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.io.Reader
import java.io.StringReader
import java.nio.file.Files
import java.nio.file.Paths
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
    @Value("\${oauth.apple.p8-key-name}") private val keyName: String,
    @Value("\${oauth.apple.p8-key}") private val privateKey: String
) {
    private val SIX_MONTHS_IN_MILLISECONDS = 1L * 30 * 24 * 60 * 60 * 1000

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
        .signWith(loadPrivateKey(privateKey), Jwts.SIG.ES256)
        .compact()

    private fun loadPrivateKey(privateKey: String): PrivateKey {
//        val resource = ClassPathResource(filename)
//        val keyBytes = resource.inputStream.readAllBytes()
        val keyContent = Base64.getDecoder().decode(privateKey)
        val keySpec =  PKCS8EncodedKeySpec(keyContent)
        val keyFactory = KeyFactory.getInstance("EC")

//        val resource = ClassPathResource(keyName)
//        val privateKey = String(Files.readAllBytes(Paths.get(resource.uri)))
//        val pemReader: Reader = StringReader(privateKey)
//        val pemParser: PEMParser = PEMParser(pemReader)
//        val converter: JcaPEMKeyConverter = JcaPEMKeyConverter()
//        val privateKeyInfo : PrivateKeyInfo = pemParser.readObject() as PrivateKeyInfo

        return keyFactory.generatePrivate(keySpec)
        //return converter.getPrivateKey(privateKeyInfo)

    }

}
package com.be.shoackserver.config

import com.eatthepath.pushy.apns.ApnsClient
import com.eatthepath.pushy.apns.ApnsClientBuilder
import com.eatthepath.pushy.apns.auth.ApnsSigningKey
import lombok.extern.log4j.Log4j2
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File

@Log4j2
@Configuration
class ApnsConfig(
    @Value("\${oauth.apple.p8-key-name}") private val pathName: String,
    @Value("\${oauth.apple.team-id}") private val teamId: String,
    @Value("\${oauth.apple.key-id}") private val keyId: String
) {
/**
 * 개발환경 사용 (배포환경이라면 PRODUCTION_APNS_HOST 사용)
 * pathName: developer 계정의 p8 파일 경로
 * teamId: developer 계정의 Team ID
 * keyId: developer 계정의 Key ID
 * */
    @Bean
    fun apnsClient(): ApnsClient {
        val keyInputStream = this::class.java.classLoader.getResourceAsStream(pathName)
            ?: throw IllegalStateException("APNS key file not found in classpath")

        val signingKey = ApnsSigningKey.loadFromInputStream(keyInputStream, teamId, keyId)

        return ApnsClientBuilder()
            .setApnsServer(ApnsClientBuilder.DEVELOPMENT_APNS_HOST)
            .setSigningKey(signingKey)
            .build()
    }
}
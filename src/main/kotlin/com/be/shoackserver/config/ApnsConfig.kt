package com.be.shoackserver.config

import com.eatthepath.pushy.apns.ApnsClient
import com.eatthepath.pushy.apns.ApnsClientBuilder
import com.eatthepath.pushy.apns.auth.ApnsSigningKey
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File


@Configuration
class ApnsConfig {

    @Bean
    fun apnsClient(): ApnsClient {
        val resourceStream = javaClass.getResourceAsStream("/AuthKey_DXK7GC475X.p8")
        return ApnsClientBuilder()
            // 개발환경 사용 (배포환경이라면 PRODUCTION_APNS_HOST 사용)
            .setApnsServer(ApnsClientBuilder.DEVELOPMENT_APNS_HOST)
            /*
            * pathName: developer 계정의 p8 파일 경로
            * teamId: developer 계정의 Team ID
            * keyId: developer 계정의 Key ID
            * */

            .setSigningKey(ApnsSigningKey.loadFromPkcs8File(
                File("src/main/resources/AuthKey_DXK7GC475X.p8"),
                "R62RZ89ZRU", "DXK7GC475X"))
            .build();
    }
}
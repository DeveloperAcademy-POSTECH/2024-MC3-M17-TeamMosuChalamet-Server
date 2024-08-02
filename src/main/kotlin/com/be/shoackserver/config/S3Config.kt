package com.be.shoackserver.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import lombok.extern.log4j.Log4j2
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Log4j2
@Configuration
class S3Config(
    @Value("\${cloud.aws.credentials.access-key}")
    private var accessKey: String,

    @Value("\${cloud.aws.credentials.secret-key}")
    private val secretKey: String,

    @Value("\${cloud.aws.region.static}")
    private val region: String
) {
    @Bean
    fun amazonS3Client(): AmazonS3 {
        return AmazonS3ClientBuilder.standard()
            .withCredentials(
                AWSStaticCredentialsProvider(BasicAWSCredentials(accessKey, secretKey))
            )
            .withRegion(region)
            .build()
    }
}
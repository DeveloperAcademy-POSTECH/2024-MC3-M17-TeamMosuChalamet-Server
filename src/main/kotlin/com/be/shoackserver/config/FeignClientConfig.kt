package com.be.shoackserver.config

import com.be.shoackserver.ShoackServerApplication
import org.springframework.context.annotation.Configuration
import org.springframework.cloud.openfeign.EnableFeignClients

@Configuration
@EnableFeignClients(basePackageClasses = [ShoackServerApplication::class])
class FeignClientConfig {
}
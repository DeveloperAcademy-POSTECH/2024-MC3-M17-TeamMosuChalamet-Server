package com.be.shoackserver.config

import com.be.shoackserver.ShoackServerApplication
import lombok.extern.log4j.Log4j2
import org.springframework.context.annotation.Configuration
import org.springframework.cloud.openfeign.EnableFeignClients

@Log4j2
@Configuration
@EnableFeignClients(basePackageClasses = [ShoackServerApplication::class])
class FeignClientConfig {
}
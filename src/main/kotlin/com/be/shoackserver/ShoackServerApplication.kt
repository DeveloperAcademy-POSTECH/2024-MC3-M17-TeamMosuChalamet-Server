package com.be.shoackserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class ShoackServerApplication

fun main(args: Array<String>) {
    runApplication<ShoackServerApplication>(*args)
}

package com.be.shoackserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ShoackServerApplication

fun main(args: Array<String>) {
    runApplication<ShoackServerApplication>(*args)
}

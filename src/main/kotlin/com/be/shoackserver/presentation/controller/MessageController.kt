package com.be.shoackserver.presentation.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class MessageController {

    @PostMapping("/message")
    fun sendMessage() {
    }
}
package com.be.shoackserver.presentation.controller

import com.be.shoackserver.application.usecase.MessageUseCase
import com.be.shoackserver.presentation.request.MessageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api")
class MessageController(
    private val messageUseCase: MessageUseCase
) {

    @PostMapping("/message")
    fun sendMessage(@RequestBody messageRequest: MessageRequest) : ResponseEntity<Void> {
        messageUseCase.sendMessage(messageRequest.destinationMemberId?.let { it }
            ?: throw IllegalStateException("Destination member id is null"))
        return ResponseEntity.created(URI.create("/")).build()
    }
}
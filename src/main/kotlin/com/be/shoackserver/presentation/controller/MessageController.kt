package com.be.shoackserver.presentation.controller

import com.be.shoackserver.application.usecase.MessageUseCase
import com.be.shoackserver.presentation.request.IdRequest
import lombok.extern.log4j.Log4j2
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@Log4j2
@RestController
@RequestMapping("/api")
class MessageController(
    private val messageUseCase: MessageUseCase
) {

    @PostMapping("/message")
    fun sendMessage(@RequestBody idRequest: IdRequest) : ResponseEntity<Void> {
        messageUseCase.sendMessage(
            idRequest.id
            ?: throw IllegalStateException("Destination member id must not be null"))
        return ResponseEntity.created(URI.create("/")).build()
    }
}
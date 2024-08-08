package com.be.shoackserver.presentation.controller

import com.be.shoackserver.application.usecase.FriendUseCase
import com.be.shoackserver.presentation.request.IdRequest
import com.be.shoackserver.presentation.response.FriendResponse
import lombok.extern.log4j.Log4j2
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@Log4j2
@RestController
@RequestMapping("/api")
class FriendController (
    private val friendUseCase: FriendUseCase
) {

    @GetMapping("/friend")
    fun getFriendList() : ResponseEntity<List<FriendResponse>> {
        return ResponseEntity.ok(friendUseCase.loadFriendList())
    }

    @PostMapping("/friend")
    fun addFriend(@RequestBody idRequest: IdRequest) : ResponseEntity<Void>{
        friendUseCase.allowFriendshipRequest(idRequest.id ?: throw
        IllegalArgumentException("requester id must not be null"))
        return ResponseEntity.created(URI.create("/friend")).build()
    }

    @DeleteMapping("/friend")
    fun deleteFriend(@RequestBody idRequest: IdRequest) : ResponseEntity<Void> {
        friendUseCase.deleteFriend(idRequest.id ?: throw
        IllegalArgumentException("friend id must not be null"))
        return ResponseEntity.ok().build()
    }
}
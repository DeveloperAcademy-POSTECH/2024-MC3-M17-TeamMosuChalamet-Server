package com.be.shoackserver.presentation.controller

import com.be.shoackserver.application.dto.MemberDto
import com.be.shoackserver.application.service.FriendService
import com.be.shoackserver.presentation.response.FriendResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController()
@RequestMapping("/api")
class FriendController (

    private val friendService : FriendService
) {
    @GetMapping("/friend")
    fun getFriendList() : ResponseEntity<List<FriendResponse>> {

        val friendList = friendService.getFriendList().map { FriendResponse.of(it) }
        return ResponseEntity.ok(friendList)
    }

    @PostMapping("/friend")
    fun addFriend(@RequestParam requesterId : Long) : ResponseEntity<Void>{

        friendService.saveFriendship(requesterId)

        return ResponseEntity.created(URI.create("/friend")).build()
    }
}
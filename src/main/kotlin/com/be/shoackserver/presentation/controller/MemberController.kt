package com.be.shoackserver.presentation.controller

import com.be.shoackserver.application.service.MemberService
import com.be.shoackserver.presentation.response.ProfileResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController()
@RequestMapping("/api")
class MemberController(
    private val memberService: MemberService
){

    @GetMapping("/profile")
    fun getProfile() : ResponseEntity<ProfileResponse> {

        val memberDto = memberService.getMember()
        return ResponseEntity.ok(ProfileResponse.of(memberDto))
    }

    @PatchMapping("/profile")
    fun updateProfile() {
    }
}
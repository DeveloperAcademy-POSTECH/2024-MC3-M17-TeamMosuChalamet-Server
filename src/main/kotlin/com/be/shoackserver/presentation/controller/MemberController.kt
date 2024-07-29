package com.be.shoackserver.presentation.controller

import com.be.shoackserver.application.service.MemberService
import com.be.shoackserver.application.usecase.ProfileUseCase
import com.be.shoackserver.presentation.response.ProfileResponse
import lombok.extern.log4j.Log4j2
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Log4j2
@RestController
@RequestMapping("/api")
class MemberController(
    private val profileUseCase: ProfileUseCase
){

    @GetMapping("/profile")
    fun getProfile() : ResponseEntity<ProfileResponse> {
        return ResponseEntity.ok(profileUseCase.loadProfile())
    }

    @PatchMapping("/profile")
    fun updateProfile(@RequestPart profileImage: MultipartFile) : ResponseEntity<ProfileResponse>{
        profileUseCase.updateProfileImage(profileImage)
        return ResponseEntity.ok(profileUseCase.loadProfile())
    }
}
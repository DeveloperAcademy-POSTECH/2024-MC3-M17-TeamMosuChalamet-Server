package com.be.shoackserver.presentation.controller

import com.be.shoackserver.application.usecase.MemberManageUseCase
import com.be.shoackserver.application.usecase.ProfileUseCase
import com.be.shoackserver.presentation.request.DeviceTokenRequest
import com.be.shoackserver.presentation.request.ProfileNameRequest
import com.be.shoackserver.presentation.response.ProfileResponse
import lombok.extern.log4j.Log4j2
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Log4j2
@RestController
@RequestMapping("/api")
class MemberController(
    private val profileUseCase: ProfileUseCase,
    private val memberManageUseCase: MemberManageUseCase
){

    @GetMapping("/profile")
    fun getProfile() : ResponseEntity<ProfileResponse> {
        return ResponseEntity.ok(profileUseCase.loadProfile())
    }

    @PatchMapping("/profile/image")
    fun updateProfile(@RequestPart profileImage: MultipartFile) : ResponseEntity<ProfileResponse>{
        profileUseCase.updateProfileImage(profileImage)
        return ResponseEntity.ok(profileUseCase.loadProfile())
    }

    @PatchMapping("/deviceToken")
    fun updateDeviceToken(@RequestBody request: DeviceTokenRequest): ResponseEntity<Void> {
        memberManageUseCase.updateDeviceToken(
            request.deviceToken
            ?: throw IllegalStateException("Device token is null"))
        return ResponseEntity.ok().build()
    }

    @PatchMapping("/profile/name")
    fun updateProfileName(@RequestBody request: ProfileNameRequest) : ResponseEntity<ProfileResponse> {
        profileUseCase.updateProfileName(request.name?: throw IllegalStateException("Name is null"))
        return ResponseEntity.ok(profileUseCase.loadProfile())
    }

    @DeleteMapping("/member")
    fun deleteMember() : ResponseEntity<Void> {
        memberManageUseCase.deleteMember()
        return ResponseEntity.ok().build()
    }
}
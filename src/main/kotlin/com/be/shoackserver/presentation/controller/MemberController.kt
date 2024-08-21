package com.be.shoackserver.presentation.controller

import com.be.shoackserver.application.usecase.LoginUseCase
import com.be.shoackserver.application.usecase.MemberManageUseCase
import com.be.shoackserver.application.usecase.ProfileUseCase
import com.be.shoackserver.presentation.request.DeviceTokenRequest
import com.be.shoackserver.presentation.request.ProfileNameRequest
import com.be.shoackserver.presentation.response.ProfileResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api")
class MemberController(
    private val profileUseCase: ProfileUseCase,
    private val memberManageUseCase: MemberManageUseCase,
    private val loginUseCase: LoginUseCase
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
    fun deleteMember(request: HttpServletRequest) : ResponseEntity<Void> {
        val userAgentHeader = request.getHeader("User-Agent") ?: throw IllegalArgumentException("User-Agent is null")
        val userAgent = userAgentHeader.contains("AppClip").let { if (it) "appClip" else "app" }
        memberManageUseCase.deleteMember(userAgent)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/logout")
    fun logout(request: HttpServletRequest) : ResponseEntity<Void> {
        loginUseCase.signOut(request)
        return ResponseEntity.ok().build()
    }
}
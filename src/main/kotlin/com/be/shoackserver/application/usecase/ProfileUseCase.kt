package com.be.shoackserver.application.usecase

import com.be.shoackserver.application.dto.MemberDto
import com.be.shoackserver.application.service.AuthenticationService
import com.be.shoackserver.application.service.ImageService
import com.be.shoackserver.application.service.MemberService
import com.be.shoackserver.presentation.response.ProfileResponse
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ProfileUseCase(
    private val memberService: MemberService,
    private val imageService: ImageService,
    private val authenticationService: AuthenticationService
) {
    private fun getMemberId() : Long {
        return authenticationService.getMemberIdFromSecurityContext()
    }

    fun loadProfile() : ProfileResponse{
        val memberDto = MemberDto.of(memberService.getMember(getMemberId()))
        memberDto.imageName = memberDto.imageName?.let { imageService.generateS3URL("profile", it) }// ?: throw IllegalStateException("Image name is null")
        return ProfileResponse.of(memberDto)
    }

    fun updateProfileImage(multipartFile: MultipartFile) {
        // 기존 프로필 이미지 삭제
        memberService.getMember(getMemberId()).imageName?.let { imageService.deleteImage("profile", it) }// ?: throw IllegalStateException("Image name is null")
        val imageName = imageService.uploadMultipartFile(multipartFile, "profile")
        memberService.updateMemberProfileImageName(imageName, getMemberId())

    }
}
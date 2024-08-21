package com.be.shoackserver.application.service

import com.be.shoackserver.application.dto.MemberDto
import com.eatthepath.pushy.apns.ApnsClient
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.stereotype.Service
import kotlin.test.Test

@Service
class MessageServiceTest {

    private lateinit var apnsClient: ApnsClient

    private lateinit var messageService: MessageService

    @BeforeEach
    fun setUp() {
        //MockitoAnnotations.openMocks(this)
        messageService = MessageService(apnsClient, "ADA.mosuchalamet.Shoak.watchkitapp")
    }

    @Test
    fun appClipTest() {
        // Given
        val memberDto = MemberDto(1L, "appleUserId", "test user", "imageName", "deviceToken", "appleRefreshToken", "userAgent", "role")
        val deviceToken = "8e689bdf345a5d673d5ea7371a371ef567fd3111092775d45b5ae1939dec106"
        val topic = "ADA.mosuchalamet.Shoak"

        // When
        messageService.sendPushNotification(memberDto, deviceToken, topic)
        // Then
    }

    @AfterEach
    fun tearDown() {
    }
}
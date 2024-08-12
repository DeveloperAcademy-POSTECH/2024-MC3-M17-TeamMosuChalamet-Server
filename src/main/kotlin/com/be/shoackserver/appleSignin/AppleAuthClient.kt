package com.be.shoackserver.appleSignin

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "apple-auth-client", url = "https://appleid.apple.com/auth")
interface AppleAuthClient {

    @GetMapping("/keys")
    fun getApplePublicKeys() : ApplePublicKeys

    @PostMapping("/token", consumes = ["application/x-www-form-urlencoded"])
    fun getAppleTokens(appleTokenRequest: AppleTokenRequest) : ResponseEntity<AppleTokenResponse>

    @PostMapping("/revoke", consumes = ["application/x-www-form-urlencoded"])
    fun revokeAppleToken(appleRevokeRequest: AppleRevokeRequest)
}

data class AppleTokenRequest(
    var client_id: String,
    var client_secret: String,
    var code: String,
    var grant_type: String
)

data class AppleTokenResponse(
    val access_token: String,
    val expires_in: Int,
    val id_token: String,
    val refresh_token: String,
    val token_type: String
)

data class AppleRevokeRequest(
    var client_id: String,
    var client_secret: String,
    var token: String,
    var token_type_hint: String
)
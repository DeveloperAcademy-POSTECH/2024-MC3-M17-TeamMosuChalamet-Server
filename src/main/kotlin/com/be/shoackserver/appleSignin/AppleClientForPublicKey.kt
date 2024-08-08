package com.be.shoackserver.appleSignin

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "apple-auth-client", url = "https://appleid.apple.com/auth")
interface AppleAuthClient {

    @GetMapping("/keys")
    fun getApplePublicKeys() : ApplePublicKeys

    @PostMapping("/token")
    fun getAppleTokens(@RequestBody appleTokenRequest: AppleTokenRequest) : AppleTokenResponse

    @PostMapping("/revoke")
    fun revokeAppleToken(@RequestBody appleSignInRevokeRequest: AppleSignInRevokeRequest)
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

data class AppleSignInRevokeRequest(
    val client_id: String,
    val client_secret: String,
    val token: String,
    val token_type_hint: String
)
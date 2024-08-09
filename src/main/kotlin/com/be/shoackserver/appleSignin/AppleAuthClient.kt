package com.be.shoackserver.appleSignin

import lombok.Data
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
    fun revokeAppleToken(appleSignInRevokeRequest: AppleSignInRevokeRequest)
}

@Data
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
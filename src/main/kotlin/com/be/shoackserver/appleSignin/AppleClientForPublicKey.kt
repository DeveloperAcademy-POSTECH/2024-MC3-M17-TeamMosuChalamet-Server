package com.be.shoackserver.appleSignin

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(name = "apple-public-key-client", url = "https://appleid.apple.com/auth")
interface AppleClientForPublicKey {

    @GetMapping("/keys")
    fun getApplePublicKeys() : ApplePublicKeys
}
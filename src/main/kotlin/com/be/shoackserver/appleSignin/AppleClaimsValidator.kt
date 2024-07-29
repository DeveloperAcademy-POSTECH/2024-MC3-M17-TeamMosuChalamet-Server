//package com.be.shoackserver.appleSignin
//
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.stereotype.Component
//
//@Component
//class AppleClaimsValidator(
//    @Value("\${oauth.apple.iss}") private val iss: String,
//    @Value("\${oauth.apple.cliendt-id}") private val clientId: String,
//    @Value("\${oauth.apple.nonce}") private val nonce: String,
//) {
//    private val encryptedNonce: String = EncryptUtils.encrypt(nonce)
//
//    companion object {
//        private const val NONCE_KEY = "nonce"
//    }
//}
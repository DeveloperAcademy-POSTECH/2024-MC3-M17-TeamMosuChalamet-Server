package com.be.shoackserver.appleSignin

import java.security.MessageDigest

class HashUtils {

    companion object {
        fun hash(nonce: String): String {

            return try {
                val sha256 = MessageDigest.getInstance("SHA-256")
                val digest = sha256.digest(
            }
        }
    }
}
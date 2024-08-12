package com.be.shoackserver

import com.be.shoackserver.appleSignin.AppleAuthClient
import com.be.shoackserver.appleSignin.ApplePublicKey
import com.be.shoackserver.appleSignin.ApplePublicKeys
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import kotlin.test.Test


@SpringBootTest
class AppleClientTest {

    @Autowired
    private lateinit var appleClient : AppleAuthClient

    @Test
    @DisplayName("apple 서버와 통신하여 Apple public keys 응답을 받는다")
    fun getPublicKeys() {
        val applePublicKeys: ApplePublicKeys = appleClient.getApplePublicKeys()
        val keys: List<ApplePublicKey> = applePublicKeys.getKeys()

        val isRequestedKeysNonNull = keys.stream()
            .allMatch(this::isAllNotNull)
        assertThat(isRequestedKeysNonNull).isTrue()
    }

    private fun isAllNotNull(applePublicKey: ApplePublicKey): Boolean {
        return Objects.nonNull(applePublicKey.kty) && Objects.nonNull(applePublicKey.kid) &&
                Objects.nonNull(applePublicKey.use) && Objects.nonNull(applePublicKey.alg) &&
                Objects.nonNull(applePublicKey.n) && Objects.nonNull(applePublicKey.e)
    }

}
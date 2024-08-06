package com.be.shoackserver.appleSignin


class ApplePublicKeys{
    private var keys: List<ApplePublicKey> = mutableListOf()

    fun getMatchesKey(alg: String?, kid: String?): ApplePublicKey {
        return keys
            .stream()
            .filter { key: ApplePublicKey -> key.kid == kid && key.alg == alg }
            .findFirst()
            .orElseThrow { RuntimeException("No matching Apple public key (alg, kid) found") }
    }

    fun getKeys(): List<ApplePublicKey> {
        return keys
    }
}
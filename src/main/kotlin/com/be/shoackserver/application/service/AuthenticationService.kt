package com.be.shoackserver.application.service

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class AuthenticationService {

    fun getMemberIdFromSecurityContext() : Long {
        return SecurityContextHolder.getContext().authentication.name.toLong()
    }
}
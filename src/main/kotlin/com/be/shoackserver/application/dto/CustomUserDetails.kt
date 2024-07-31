package com.be.shoackserver.application.dto

import com.be.shoackserver.domain.entity.Member
import lombok.RequiredArgsConstructor
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder


class CustomUserDetails(private val member: Member) : UserDetails {


    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(GrantedAuthority { member.role })
    }

    fun bCryptPasswordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    override fun getPassword(): String = bCryptPasswordEncoder().encode(member.name) // 인코딩 해야 autheticationManager에서 비교 가능

    override fun getUsername(): String = member.id.toString()

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}


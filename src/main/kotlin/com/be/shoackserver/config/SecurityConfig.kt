package com.be.shoackserver.config

import com.be.shoackserver.application.service.MemberService
import com.be.shoackserver.application.usecase.LoginUseCase
import com.be.shoackserver.application.usecase.MemberManageUseCase
import com.be.shoackserver.domain.repository.RefreshRepository
import com.be.shoackserver.exception.ExceptionHandlerFilter
import com.be.shoackserver.jwt.JWTFilter
import com.be.shoackserver.jwt.JWTUtil
import com.be.shoackserver.jwt.LoginFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val authenticationConfiguration: AuthenticationConfiguration,
    private val jwtUtil: JWTUtil,
    private val loginUseCase: LoginUseCase,
    private val memberManageUseCase: MemberManageUseCase,
    private val memberService: MemberService
) {

    @Bean
    fun authenticationManager(configuration: AuthenticationConfiguration): AuthenticationManager =
        configuration.authenticationManager

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .logout { it.disable() }
            .authorizeRequests { authorize ->
                authorize
                    .requestMatchers("/login", "/api/reissue").permitAll()
                    .requestMatchers("/api/admin").hasRole("ADMIN")
                    .anyRequest().authenticated()
            }
            .addFilterBefore(JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(ExceptionHandlerFilter(), JWTFilter::class.java)
            .addFilterAt(LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, loginUseCase, memberManageUseCase, memberService), UsernamePasswordAuthenticationFilter::class.java)
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }

        return http.build()
    }
}
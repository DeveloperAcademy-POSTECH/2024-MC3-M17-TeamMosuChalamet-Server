package com.be.shoackserver.jwt

import com.be.shoackserver.application.dto.CustomUserDetails
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class LoginFilter(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JWTUtil
) : UsernamePasswordAuthenticationFilter() {

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val identityToken = request.getParameter("identityToken")//obtainUsername(request)
        val name = request.getParameter("name")//obtainPassword(request)
        val deviceToken = request.getParameter("deviceToken")

        // 애플 로그인 등장!

        val authenticationToken = UsernamePasswordAuthenticationToken(identityToken, name, null)

        return authenticationManager.authenticate(authenticationToken)
    }

    override fun successfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain, authResult: Authentication) {
        val customUserDetails = authResult.principal as CustomUserDetails
        val username = customUserDetails.username
        val role = customUserDetails.authorities.first().authority

        // jwt 생성
        val token = jwtUtil.generateToken(username, role, 30 * 24 * 60 * 60 * 1000L) // 30일

        response.addHeader("Authorization", "Bearer $token")

        println("success")
    }

    override fun unsuccessfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, failed: AuthenticationException) {
        // 회원정보 저장로직

        response.status = 401
        println("failed")
    }
}

//package com.be.shoackserver.jwt;
//
//import com.be.shoackserver.application.dto.CustomUserDetails;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.apache.sshd.client.auth.pubkey.UserAuthPublicKeyIterator;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@RequiredArgsConstructor
//public class LoginFilter extends UsernamePasswordAuthenticationFilter {
//
//    private final AuthenticationManager authenticationManager;
//    private final JWTUtil jwtUtil;
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
//        String username = obtainUsername(request);
//        String password = obtainPassword(request);
//
//        System.out.println("username: " + username);
//        System.out.println("password: " + password);
//
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
//
//        return authenticationManager.authenticate(authenticationToken);
//
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
//        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
//        String username = customUserDetails.getUsername();
//        String role = customUserDetails.getAuthorities().stream().findFirst().get().getAuthority();
//
//        // jwt 생성
//        String token = jwtUtil.generateToken(username, role, 60 * 60 * 10L);
//
//        response.addHeader("Authorization", "Bearer " + token);
//
//        System.out.println("success");
//    }
//
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
//
//        response.setStatus(401);
//        System.out.println("failed");
//    }
//}

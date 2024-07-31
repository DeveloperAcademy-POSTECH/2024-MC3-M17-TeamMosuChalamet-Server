package com.be.shoackserver.jwt

import com.be.shoackserver.application.dto.CustomUserDetails
import com.be.shoackserver.domain.entity.Member
import com.be.shoackserver.domain.repository.MemberRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JWTFilter(private val jwtUtil: JWTUtil) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val authorizationHeader = request.getHeader("Authorization")

        when {
            authorizationHeader == null || !authorizationHeader.startsWith("Bearer ") -> {
                println("token null")
                filterChain.doFilter(request, response)
                return
            }
            else -> {
                val token = authorizationHeader.split(" ")[1]
                println("token: $token")
                if (jwtUtil.isExpired(token)) {
                    println("token expired")
                    filterChain.doFilter(request, response)
                    return
                }

                val username = jwtUtil.getUsername(token)
                val role = jwtUtil.getRole(token)

                println("username: $username")
                println("role: $role")

                val member = Member().apply {
                    this.id = username.toLong()
                    this.role = role
                }

                val customUserDetails = CustomUserDetails(member)

                val authToken: Authentication = UsernamePasswordAuthenticationToken(
                    customUserDetails, "a", customUserDetails.authorities
                )

                SecurityContextHolder.getContext().authentication = authToken

                filterChain.doFilter(request, response)
            }
        }
    }
}
//
//package com.be.shoackserver.jwt;
//
//import com.be.shoackserver.application.dto.CustomUserDetails;
//import com.be.shoackserver.domain.entity.Member;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@RequiredArgsConstructor
//class JWTFilter extends OncePerRequestFilter {
//
//    private final JWTUtil jwtUtil;
//
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String authorizationHeader = request.getHeader("Authorization");
//
//        // Authorization 헤더 검증
//        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
//
//            System.out.println("token null");
//            filterChain.doFilter(request, response); // 다음 필터로 넘어감
//
//            return;
//        }
//
//        String token = authorizationHeader.split(" ")[1]; // Bearer 다음에 있는 토큰 추출
//
//        // 토큰 유효기간 검증
//        if (jwtUtil.isExpired(token)) {
//            System.out.println("token expired");
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String username = jwtUtil.getUsername(token);
//        String role = jwtUtil.getRole(token);
//
//        // Member 객체 생성
//        Member member = new Member();
//        member.setAppleUserId(username);
//        member.setRole(role);
//
//        // UserDetails 객체 생성
//        CustomUserDetails customUserDetails = new CustomUserDetails(member);
//
//        // 스프링 시큐리티 인증 토큰 생성
//        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, "", customUserDetails.getAuthorities());
//
//        // SecurityContext에 인증 토큰 저장
//        SecurityContextHolder.getContext().setAuthentication(authToken);
//
//        filterChain.doFilter(request, response);
//
//    }
//}

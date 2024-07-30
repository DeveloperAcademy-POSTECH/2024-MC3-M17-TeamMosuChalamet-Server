package com.be.shoackserver.application.service

import com.be.shoackserver.application.dto.CustomUserDetails
import com.be.shoackserver.domain.repository.MemberRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(private val memberRepository: MemberRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val member = memberRepository.findMemberByAppleUserId(username)
            ?: throw UsernameNotFoundException("User not found with username: $username")
        return CustomUserDetails(member)
    }
}





//@Service
//@RequiredArgsConstructor
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final MemberRepository memberRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        Member member = memberRepository.findMemberByAppleUserId(username);
//
//        if (member != null) {
//            return new CustomUserDetails(member);
//        }
//
//        return null;
//    }
//}

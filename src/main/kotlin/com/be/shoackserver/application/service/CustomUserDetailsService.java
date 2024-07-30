package com.be.shoackserver.application.service;

import com.be.shoackserver.application.dto.CustomUserDetails;
import com.be.shoackserver.domain.entity.Member;
import com.be.shoackserver.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findMemberByAppleUserId(username);

        if (member != null) {
            return new CustomUserDetails(member);
        }

        return null;
    }
}

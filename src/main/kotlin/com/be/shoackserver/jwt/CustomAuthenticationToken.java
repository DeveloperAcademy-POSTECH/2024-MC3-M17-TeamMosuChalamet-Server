//package com.be.shoackserver.jwt;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.AbstractAuthenticationToken;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//
//import java.util.Collection;
//
//
//public class CustomAuthenticationToken extends AbstractAuthenticationToken {
//
//    private final Object principal;
//    private Object credentials;
//
//    public CustomAuthenticationToken(Object principal, Object credentials) {
//        super((Collection)null);
//        this.principal = principal;
//        this.credentials = credentials;
//        this.setAuthenticated(false);
//    }
//
//    public CustomAuthenticationToken(Collection<? extends GrantedAuthority> authorities, Object principal, Object credentials) {
//        super(authorities);
//        this.principal = principal;
//        this.credentials = credentials;
//    }
//
//    @Override
//    public Object getCredentials() {
//        return null;
//    }
//
//    @Override
//    public Object getPrincipal() {
//        return null;
//    }
//}

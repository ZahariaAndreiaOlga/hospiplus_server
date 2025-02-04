package com.hospi.hospiplus.utils;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class CustomAuthenticationToken extends AbstractAuthenticationToken {

    private final String email;
    private final Integer userId;
    private final String role;

    public CustomAuthenticationToken(String email, String role, Integer userId) {
        super(List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())));
        this.email = email;
        this.userId = userId;
        this.role = role;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.email;
    }

    public Integer getUserId(){
        return this.userId;
    }

    public String getRole(){
        return this.role;
    }
}

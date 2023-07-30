package com.yezi.secretgarden.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;



public class PrincipalDetails implements UserDetails {


    String username;
    String auth;
    String password;
    public PrincipalDetails(String username, String auth, String password) {
        this.username = username;
        this.auth = auth;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        String[] auths = auth.split(",");
        for(String auth : auths) {
            authorities.add(new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return auth;
                }
            });
        }
        return authorities;
    }

    public String getStringAuth() {
        return this.auth;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

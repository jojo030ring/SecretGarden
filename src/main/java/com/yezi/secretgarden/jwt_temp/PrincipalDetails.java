package com.yezi.secretgarden.jwt_temp;

import com.yezi.secretgarden.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


public class PrincipalDetails implements UserDetails {



    String username;
    String auth;
    public PrincipalDetails(String id, String auth) {
        this.username = username;
        this.auth = auth;


    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        Arrays.stream(auth.split(",")).forEach(r->{
            authorities.add(()-> r);
        });
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
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

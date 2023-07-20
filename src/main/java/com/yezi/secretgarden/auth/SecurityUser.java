package com.yezi.secretgarden.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityUser implements UserDetails {
    private static final long serialVersionUID = 1L;


    @Override
    /**
     * 유저 권한 생성
     */
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new LinkedList<>();
        list.add(new SimpleGrantedAuthority("auth1"));
        list.add(new SimpleGrantedAuthority("auth2"));
        return list;
    }

    @Override
    public String getPassword() {
        return "$2a$10$SDBxd18/9SovlON7h/HewOwTe/drGLIx/UV0G0k91qLRWnGz0VoR.";  //1234 > 하드코딩되어있는거 DB 매핑할 수 있게 고치기
    }

    @Override
    public String getUsername() {
        return "admin";
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}

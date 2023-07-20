package com.yezi.secretgarden.service;

import com.yezi.secretgarden.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class SecurityService implements UserDetailsService {
    private UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUser(username);// DB에 갔다 온 결과가 필요
        UserDetails build = null;

        try {
            org.springframework.security.core.userdetails.User.UserBuilder userBuilder
                    = org.springframework.security.core.userdetails.User.withUsername(username).password(user.getPassword());
            userBuilder.authorities(user.getAutorities());
            build = userBuilder.build();
        }
    }
}

// https://lts0606.tistory.com/669
// https://zest133.tistory.com/entry/Spring-Security-login2
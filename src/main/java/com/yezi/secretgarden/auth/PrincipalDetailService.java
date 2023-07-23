package com.yezi.secretgarden.auth;

import com.yezi.secretgarden.domain.User;
import com.yezi.secretgarden.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// localhost:8080/login>> 스프링 시큐리티의 기본 로그인
// 해당 요청이 들어오면 loadUserByUsername메서드가 자동으로 실행된다
@Service
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService {
    private final UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailsService : 진입");
        User user = userService.findUser(username); // DB에 넣어야 값이 나옵니다
        if(user==null) throw new UsernameNotFoundException("존재하지 않는 사용자입니다.");
        return new PrincipalDetails(user);
    }
}

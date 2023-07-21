package com.yezi.secretgarden.auth;

import com.yezi.secretgarden.domain.User;
import com.yezi.secretgarden.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

/**
 * 시큐리티 설정에서 loginProcessingUrl("/login")으로 걸어놔서
 * /login 요청이 오면 자동으로 UserDetailsService 타입으로 IOC 되어있는 loadUserByUsername 함수가 자동으로실행
 *
 */

@RequiredArgsConstructor
public class PrincipalDetailsService1 implements UserDetailsService {
    /**
     * 여기서 중요한 점. form 태그의 name을 username이라고 지정하지 않으면 값이 들어오지 않는다...
     * 하지만 나는 id로 저장해놨는데.. >> config 파일을 손보자     */

    private final UserService userService;
    @Override
// 시큐리티 session(내부 Authentication(내부 UserDetails(내부 User)))
    @Transactional
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User userEntity = userService.findUser(id);
        System.out.println(userEntity);
        if(userEntity!=null) {
            return
                    new PrincipalDetails1(userEntity);
        }
        return null;
    }
}

package com.yezi.secretgarden.auth;

import com.yezi.secretgarden.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

// 시큐리티가 /login 주소요청을 낚아채서 로그인을 진행시킴
// 로그인 진행이 완료가 되면 session을 만들어준다
// 시큐리티가 가지고 있는 session(같은 세션 공간임) Security ContextHolder<< 에다 세션정보를 저장시킨다
// 오브젝트 > Authentication 타입의 객체
// Authentication 안에는 User 정보가 있어야하는데,
// UserDetails 타입 객체가 Authentication 안에 들어갈 수 있으므로 User 오브젝트를 넣어준다.
// 시큐리티가 가지고 있는 세션 영역(Security Session)에 Authentication 객체가 저장되는데, 이 안에 있는 User정보는 UserDetails가 저장된다.
// 현재 구현하려고 하니 UserDetails는 PrincipalDetails가 된다
public class PrincipalDetails implements UserDetails {
    private User user;
    public PrincipalDetails(User user) {
        this.user= user;
    }
    // 해당 유저의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        user.getRole(); 인데 타입이 저렇게 정해져있는거임
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }

    // 이 계정 만료 안되었지?
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    // 이 계정 잠기지 않았지?
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    // 비번 넘 오래 안바꾸거나 뭔가 기간이 지난 거 아니지?
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    // 이 계정 활성화 된거지?
    @Override
    public boolean isEnabled() {
        // 우리 사이트에서 1년동안 회원이 로그인 안하면, 휴면계정이 되게 됨
        // 현재시간 - 로그인 시간 해서 1년을 초과하면 return 을 false하게 됨.
        return true;
    }
}

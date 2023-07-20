package com.yezi.secretgarden.filter;

import com.yezi.secretgarden.auth.JWTTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Slf4j
/**
 * 필터형태의 클래스
 * 스프링부트에서 필터란 컨트롤러 역할을 하는 메소드에게 요청이 전달되기 전에 행동을 하는 기능
 * 스프링부트에서 시큐리티는 기본적으로 서버 자원인 세션(session)을 통하여 로그인 여부를 검증 하는데,
 * 이러한 검증 방법을 JWT로 바꾸어 주기 위해서는 JWT 값을 통해 동작하는 필터클래스를 만들어 주어야함 .
 */
public class JWTTokenFilter extends GenericFilterBean {
    private JWTTokenProvider provider;
    public JWTTokenFilter(JWTTokenProvider provider) {
        this.provider=provider;
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = ((HttpServletRequest) request).getHeader(JWTTokenProvider.httpKeyValue);
        // 유효한 토큰인지 확인
        if(token != null && provider.tokenCheck(token)) {
            // 토큰이 유효하면, 토큰으로부터 유저 정보를 받아옴
            Authentication authentication = provider.authentication(token);
            // SecurityContext에 Authentication 객체를 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info(authentication.toString());
        }
        chain.doFilter(request,response);
    }
}

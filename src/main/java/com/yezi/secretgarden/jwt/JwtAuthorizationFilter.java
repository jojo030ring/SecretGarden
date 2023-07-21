package com.yezi.secretgarden.jwt;


import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * 시큐리티가 filter를 가지고 있는데, 그 필터중에 BasicAuthenticationFilter가 있다.
 * 권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 타게 되어있음
 * 만약 권한이나 인증이 필요한 주소가 아니라면 이 필터를 타지 않는다.
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    public JwtAuthorizationFilter
}

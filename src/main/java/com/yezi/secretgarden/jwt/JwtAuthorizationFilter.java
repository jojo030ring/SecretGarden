package com.yezi.secretgarden.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yezi.secretgarden.auth.PrincipalDetails;
import com.yezi.secretgarden.domain.User;
import com.yezi.secretgarden.repository.UserRepository;
import com.yezi.secretgarden.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

/**
 * 시큐리티가 filter를 가지고 있는데, 그 필터중에 BasicAuthenticationFilter가 있다.
 * 권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 타게 되어있음
 * 만약 권한이나 인증이 필요한 주소가 아니라면 이 필터를 타지 않는다.
 */

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private UserService userService;
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserService userService) {
        super(authenticationManager);
        this.userService = userService;

    }

    // 인증이나 권한이 필요한 주소 요청이 있을 때 해당 필터를 타게 됨
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        System.out.println("인증이나 권한이 필요한 주소가 요청이 됨");
        Cookie cookie = getCookie(request.getCookies());
        String jwt = (cookie == null) ? null : cookie.getValue();
        System.out.println("jwt from cookie : " + jwt);


        // jwt 토큰을 검증해서 정상적인 사용자인지 확인
//        if (jwt == null || !jwt.startsWith("Bearer")) {
        if(jwt == null){
            // 헤더가 없거나 헤더가 있는데 bearer로 시작하는경우가 아님
            doFilter(request,response,chain);
            return;
        }
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512("secretgarden")).build().verify(jwt);

        String id = decodedJWT.getClaim("id").asString();
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        System.out.println("does it have auth? "+SecurityContextHolder.getContext().getAuthentication());
        if(authentication==null) {
            if (id != null) {
                User userEntity = userService.findUser(id);
                if(userEntity == null) {
                    sendBadRequestResult(request, response, chain);
                    return;
                }
                    PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
                    // jwt 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어준다
                    authentication = new UsernamePasswordAuthenticationToken(principalDetails, userEntity.getPassword(), principalDetails.getAuthorities());
                    // 강제로 security 세션에 접근하여 Autentication 저장
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            else {
                sendBadRequestResult(request, response, chain);
                return;
            }

        }

        chain.doFilter(request, response);


    }
    public void sendBadRequestResult(HttpServletRequest request, HttpServletResponse response,FilterChain chain) throws ServletException, IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        chain.doFilter(request,response);
        return;
    }
    public Cookie getCookie(Cookie[] cookies) {
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals("token"))
                return cookie;
        }
        return null;
    }


}
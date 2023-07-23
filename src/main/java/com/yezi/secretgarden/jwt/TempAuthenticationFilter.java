package com.yezi.secretgarden.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TempAuthenticationFilter {
//    private final JwtProvider jwtProvider;
//
//    public TempAuthenticationFilter(JwtProvider jwtProvider) {
//        this.jwtProvider = jwtProvider;
//    }
//
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String token = jwtProvider.resolveToken(request);
//
//        if (token != null && jwtProvider.validateToken(token)) {
//            // check access token
//            token = token.split(" ")[1].trim();
//            Authentication auth = jwtProvider.getAuthentication(token);
//            SecurityContextHolder.getContext().setAuthentication(auth);
//        }
//
//        filterChain.doFilter(request, response);
//    }
}

package com.yezi.secretgarden.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yezi.secretgarden.auth.PrincipalDetails;
import com.yezi.secretgarden.domain.User;
import com.yezi.secretgarden.service.UserService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Date;


/**
 * 시큐리티가 filter를 가지고 있는데, 그 필터중에 BasicAuthenticationFilter가 있다.
 * 권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 타게 되어있음
 * 만약 권한이나 인증이 필요한 주소가 아니라면 이 필터를 타지 않는다.
 */

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private UserService userService;
    private JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserService userService) {
        super(authenticationManager);
        this.userService = userService;

    }



    // 인증이나 권한이 필요한 주소 요청이 있을 때 해당 필터를 타게 됨
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwt = jwtTokenUtil.getJwtFromCookie(request);
        // jwt 토큰을 검증해서 정상적인 사용자인지 확인
        if (jwt == null || !jwt.startsWith("Bearer ")) {
            // 헤더가 없거나 헤더가 있는데 bearer로 시작하는경우가 아님
            chain.doFilter(request,response);
            return;
        }
        // 정상적인 사용자면, Bearer를 공백으로 치환
        jwt = jwt.replace("Bearer ", "");
        // id 리턴받음
        String id = jwtTokenUtil.getLoginId(jwt);

            if (id != null) {
                User userEntity = userService.findUser(id);
                if(userEntity == null) {
                    // db가 삭제되어있는데 쿠키가 남아있는 경우
                    // cookie를 삭제해준다
                    Cookie cookie = WebUtils.getCookie(request, "token");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    chain.doFilter(request,response);
                    return;
                }
                PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(
                                principalDetails,
                                null,
                                principalDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);


            }




        chain.doFilter(request,response);



    }
    public void sendBadRequestResult(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    public Cookie getCookie(Cookie[] cookies) {
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals("token"))
                return cookie;
        }
        return null;
    }


}
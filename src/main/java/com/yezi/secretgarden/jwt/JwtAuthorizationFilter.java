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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.RedirectStrategy;
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
    // spring security filter는 spring의 bean을 주입받을 수 없어 외부에서 받아옴
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserService userService) {
        super(authenticationManager);
        this.userService = userService;

    }



    // 인증이나 권한이 필요한 주소 요청이 있을 때 해당 필터를 타게 됨
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        logger.info(SecurityContextHolder.getContext());

        String jwt = jwtTokenUtil.getJwtFromCookie(request); // cookie에서 원형 jwt를 받아옴
        // jwt 토큰을 검증해서 정상적인 토큰인지 확인
        if (jwt == null || !jwt.startsWith("Bearer ")) {
            // jwt가 null이거나 jwt가 Bearer로 시작하지 않음
            // 체인을 돌려서 다음 필터로 이전해줌
            chain.doFilter(request,response);
            return;
        }
        // 정상적인 요청이면, Bearer를 공백으로 치환
        jwt = jwt.replace("Bearer ", "");
        // cookie에서 가져온 jwt를 이용하여 id를 추출함
        String id = jwtTokenUtil.getLoginId(jwt);
            // id가 null이 아닐 경우,
            if (id != null) {
                // userEntity를 받아옴
                User userEntity = userService.findUser(id);
                // userEntity가 없다 == DB에 들어있지 않다, 근데 id는 있다?
                if(userEntity == null) {
                    // db가 삭제되어있는데 쿠키는 남아있는 경우
                    // cookie를 삭제해준다
                    Cookie cookie = WebUtils.getCookie(request, "token");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    chain.doFilter(request,response); // 다음 필터로 이동시킨다
                    return;
                }
                // Authentication을 위한 PrincipalDetails를 하고 userEntity를 넣어준다.
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

}
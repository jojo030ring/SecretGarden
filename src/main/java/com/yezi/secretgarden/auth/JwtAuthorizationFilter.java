package com.yezi.secretgarden.auth;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yezi.secretgarden.auth.PrincipalDetails;
import com.yezi.secretgarden.domain.User;
import com.yezi.secretgarden.jwt.JwtTokenUtil;
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
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.Principal;
import java.util.Date;


/**
 * 시큐리티가 filter를 가지고 있는데, 그 필터중에 BasicAuthenticationFilter가 있다.
 * 권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 타게 되어있음
 * 만약 권한이나 인증이 필요한 주소가 아니라면 이 필터를 타지 않는다.
 */

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
    // spring security filter는 spring의 bean을 주입받을 수 없어 외부에서 받아옴
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);

    }



    // 인증이나 권한이 필요한 주소 요청이 있을 때 해당 필터를 타게 됨
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String jwt = jwtTokenUtil.getPureJWT(jwtTokenUtil.decodeFromCookieToJWT(request)); // cookie에서 원형 jwt를 받아옴
        System.out.println("request.getRequestURI() = " + request.getRequestURI());
        // jwt 토큰을 검증해서 정상적인 토큰인지 확인
        /**
         * 문제가 생기는 경우
         * - 토큰이 만료된 경우 > jjwt 라이브러리의 parsing 부분에 의해 exception이 발생하고 validateToken이 이를 잡는다
         * - 토큰이 변조된 경우 >> 위와 동일
         * - null 값이 들어가는 경우 >> 위와 동일
         * - 지원되지 않는 토큰인 경우 >> 위와 동일
         */
        // 토큰이 없거나 토큰에 문제가 생긴 경우인데, 토큰이 있는 경우
        if(!jwtTokenUtil.validateToken(jwt)) {
            // token이라고 명해진 쿠키를 받아온다
            Cookie tokenInCookie = WebUtils.getCookie(request,"token");
            // token이 존재하면 cookie를 없애주어야 한다
            if(tokenInCookie != null) {
                tokenInCookie.setMaxAge(0);
                response.addCookie(tokenInCookie);

            }
            // 필터 넘김
            chain.doFilter(request,response);
            return;
        }

        // 정상적인 요청이면, Bearer를 공백으로 치환하고(JwtTokenUtil 내에서 자동적으로 이뤄짐) id, auth를 얻음
        String id = jwtTokenUtil.getLoginId(jwt);
        String auth = jwtTokenUtil.getAuth(jwt);
        // id가 null이 아닐 경우,
        if (id != null) {

            // db가 삭제되어있는데 쿠키는 남아있는 경우는 어떡하지?
            // 이거는 JwtAuthenticateFtiler 부분에서 걸러짐
            // authenticate를 호출하면 자동적으로 PrincipalDetailsService내의 loadby... 메서드를 호출하고 userEntity를 조회하게 된다
            // 여기서 문제가 발생하면 exception이 발생 >> unsuccessfulAuthentication 메서드로 이동, 처리
            PrincipalDetails principalDetails = new PrincipalDetails(id,auth,null); // password는 저장하지 않으므로 담아주지 않는다.
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            principalDetails,
                            null,
                            principalDetails.getAuthorities());
            // 인가처리를 위해 임시로 세션영역에 담음. 이 인가처리 부분은 하나의 요청 플로우에 잠시 담겼다가 삭제됨
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }




        chain.doFilter(request,response);



    }

}

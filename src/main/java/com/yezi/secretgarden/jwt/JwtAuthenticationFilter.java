package com.yezi.secretgarden.jwt;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.querydsl.jpa.hibernate.SessionHolder;
import com.yezi.secretgarden.auth.LoginFailHandler;
import com.yezi.secretgarden.auth.PrincipalDetails;

import com.yezi.secretgarden.domain.request.UserRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
@Slf4j
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    // json 데이터를 객체 형태로 파싱해주는 objectMapper를 생성
    private ObjectMapper objectMapper = new ObjectMapper();
    // JWTToken과 관련된 클래스를 생성
    private JwtTokenUtil provider = new JwtTokenUtil();
    /**
     * Spring security가 제공하는 login page를 다른 것으로 변환
     */
    private static final AntPathRequestMatcher DEFAULT_PATH_REQUEST_MATCHER
            = new AntPathRequestMatcher("/secretgarden/login","POST");
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
       super(DEFAULT_PATH_REQUEST_MATCHER,authenticationManager);

    }

    /**
     * UsernamePasswordAuthenticationFilter : 해당 필터를 거치고 다음 필터로 가지 않는다.
     * 인증 성공 여부에 따른 메서드 successAuthentication, unSuccessfulAuthentication 구현 필수
     * 해당 필터는 /login으로 접근할 때만 동작하므로, 해당 url을 변경해주어야 한다.
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    // Authentication 객체 만들어서 리턴 => 의존 : AuthenticationManager
    // 인증 요청시에 실행되는 함수 => /login > 내가 바꿔줘서 /secretgarden/login이 됨


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        UserRequestDto userRequestDto = null;
        try {
            // request.getInputStream()안에 사용자가 로그인 폼에서 입력한 userName과 password 정보가 담겨있음
            userRequestDto = objectMapper.readValue(request.getInputStream(), UserRequestDto.class);

            // Authenticate를 위한 UsernamePasswordToken을 발행함
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            userRequestDto.getUsername(),
                            userRequestDto.getPassword());


            /**
             * <PrincipalDetailsService의 loadUserByUsername() 함수가 실행되었을 때 일어나는 일>
             * -authenticate() 함수가 호출 되면 인증 프로바이더가 유저 디테일 서비스의
             * -loadUserByUsername토큰의 첫번째 파라미터(username)를 호출하고
             * -UserDetails(PrincipalDetails)를 리턴받아서 토큰의 두번째 파라미터(credential)와
             * -UserDetails(DB값)의 getPassword()메서드로 비교해서 동일하면
             * -Authentication 객체를 만들어서 필터체인으로 리턴해준다.
             */

            // Tip: 인증 프로바이더의 디폴트 서비스는 UserDetailsService 타입
            // Tip: 인증 프로바이더의 디폴트 암호화 방식은 BCryptPasswordEncoder
            // BCryptPasswordEncoder가 자동 주입되어 신경쓰지 않아도 됨 >> bean으로 실행메서드에 적어줌

            // authencate를 실행하면, 자동으로 PrincipalService의 loadUserByUsername이 실행된다
            Authentication authentication = getAuthenticationManager().authenticate(authenticationToken);
            // authentication 객체가 session 영역에 저장됨 > 그 방법이 return authentication하는 것
            // 리턴의 이유는 권한 관리를 security 대신 해주므로 편하라고 하는 것
            return authentication;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return null;


    }

    // attemptAuthentication 실행 후 인증이 정상적으로 되었으면 successfulAuthentication 실행
    // JWT 토큰을 만들어서 request 요청한 사용자에게 JWT 토큰을 response해주면 됨
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // token을 생성하는 부분 > 미리 생성해두었던 JwtTokenUtil을 이용
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        String jwtToken = provider.createToken(principalDetails.getUsername(),principalDetails.getStringAuth());
        log.info("JwtAuthenticationFilter > 토큰생성완료!");


        Cookie cookie = new Cookie("token", URLEncoder.encode(jwtToken, StandardCharsets.UTF_8));
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.OK.value());
        response.sendRedirect("/secretgarden/board");


       log.info("JwtAuthenticationFilter > successful Authentication() 실행 완료!");

    }



}

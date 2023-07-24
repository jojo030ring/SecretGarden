package com.yezi.secretgarden.jwt;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.yezi.secretgarden.auth.LoginFailHandler;
import com.yezi.secretgarden.auth.PrincipalDetails;
import com.yezi.secretgarden.domain.User;

import com.yezi.secretgarden.domain.UserRequestDto;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private ObjectMapper objectMapper = new ObjectMapper(); // json 데이터를 파싱해줌
    LoginFailHandler loginFailHandler = new LoginFailHandler();
    private JwtTokenUtil provider = new JwtTokenUtil();
    /**
     * Spring security가 제공하는 loginpage를 다른 것으로 변환
     */
    private static final AntPathRequestMatcher DEFAULT_PATH_REQUEST_MATCHER
            = new AntPathRequestMatcher("/secretgarden/login","POST");
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
       super(DEFAULT_PATH_REQUEST_MATCHER,authenticationManager);

    }

    /**
     * UsernamePasswordAuthenticationFilter : 해당 필터를 거치고 다음 필터로 가지 않는다.
     * 인증 성공 여부에 따른 메서드 successAuthentication, unSuccessfulAuthentication 구현 필수
     * 해당 필터는 login으로 접근할 때만 동작하므로, 해당 url을 변경해주어야 한다.
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

        System.out.println("JwtAuthenticationFilter : 진입");
        // request에 있는 username과 password를 파싱해서 자바 Object로 받기
        // json 데이터를 파싱해줌
        UserRequestDto userRequestDto = null;
        try {
            // stream안에 username이랑 password가 다 담겨있음
            userRequestDto = objectMapper.readValue(request.getInputStream(), UserRequestDto.class);
            System.out.println(userRequestDto);
            System.out.println("JwtAuthenticationFilter : "+userRequestDto);

            // 유저네임 패스워드 토큰 생성
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            userRequestDto.getUsername(),
                            userRequestDto.getPassword());

            System.out.println("JwtAuthenticationFilter : 토큰생성완료");
            // PrincipalDetailsService의 loadUserByUsername() 함수가 실행됨

            // authenticate() 함수가 호출 되면 인증 프로바이더가 유저 디테일 서비스의
            // loadUserByUsername(토큰의 첫번째 파라메터) 를 호출하고
            // UserDetails를 리턴받아서 토큰의 두번째 파라메터(credential)과
            // UserDetails(DB값)의 getPassword()함수로 비교해서 동일하면
            // Authentication 객체를 만들어서 필터체인으로 리턴해준다.

            // Tip: 인증 프로바이더의 디폴트 서비스는 UserDetailsService 타입
            // Tip: 인증 프로바이더의 디폴트 암호화 방식은 BCryptPasswordEncoder
            // 결론은 인증 프로바이더에게 알려줄 필요가 없음.


            // PrincipalDetailsService의 loadUserByUsername()이 실행된 후 정상이면 authentication이 리턴됨
            // DB에 있는 username과 password가 일치한다
            Authentication authentication =
                    getAuthenticationManager().authenticate(authenticationToken);
            //System.out.println("Authentication : "+principalDetailis.getUser().getUsername()); // 로그인이 정상적으로 되었다
            // authentication 객체가 session 영역에 저장됨 > 그 방법이 return 하는 것
            // 리턴의 이유는 권한 관리를 security 대신 해주므로 편하라고 하는 것
            // 굳이 JWT를 사용하면서 세션을 만들 이유는 없지만 권한처리때문에 세션에 넣어준다.
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
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        String jwtToken = provider.createToken(principalDetails.getUsername());
        System.out.println("generated jwt token : "+jwtToken);


        Cookie cookie = new Cookie("token", URLEncoder.encode(jwtToken, StandardCharsets.UTF_8));
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.OK.value());


        System.out.println("successful Authentication : 실행 완료!");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        loginFailHandler.onAuthenticationFailure(request, response, failed);


    }


}

/*
    JWT 토큰을 생성
    클라이언트쪽으로 JWT 토큰을 응답
    요청할 때마다 JWT 토큰을 가지고 요청
    서버는 JWT 토큰이 유효한지를 판단 >> 필터를 만들어야 함

 */

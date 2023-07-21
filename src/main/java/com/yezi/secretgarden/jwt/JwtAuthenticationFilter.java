package com.yezi.secretgarden.jwt;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.yezi.secretgarden.auth.PrincipalDetails;
//import com.yezi.secretgarden.domain.User;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.BufferedReader;
//import java.io.IOException;
//
///**
// * 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 있음
// * login 요청해서 username,password 전송하면(post)
// * 해당 필터가 동작을 함
// *
// * 하지만 loginform을 disable했기 때문에 이 필터는 작동되지 않는다
// * 그렇다면,,, 이 필터를 등록해주어야 한다는 것...
// *
// */
//@RequiredArgsConstructor
//public class jwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//
//    private final AuthenticationManager authenticationManager;
//
//
//    /**
//     * / login 요청을 하면 로그인 시도를 위해서 실행되는 함수 >> POST 요청에 대해 실행
//     * @param request from which to extract parameters and perform the authentication
//     * @param response the response, which may be needed if the implementation has to do a
//     * redirect as part of a multi-stage authentication process (such as OpenID).
//     * @return
//     * @throws AuthenticationException
//     */
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        System.out.println("jwt 로그인 시도중");
//
//        // 1. username, password 받아서
////        try {
////            BufferedReader br = request.getReader();
////            String input = "";
////            while((input = br.readLine())!=null) {
////                System.out.println(input);
////            }
////
////        }catch(IOException e) {
////
////        }
//
//        // json 데이터로 받는다 가정하고, 파싱해야하는데, 간단하게 해주는 게 ObjectMapper
//        ObjectMapper om = new ObjectMapper();
//        try {
//            User user = om.readValue(request.getInputStream(),User.class);
//            System.out.println(user);
//            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword());
//
//            // password는 spring이 처리를 해준다
//            // PrincipalDetailsService의 loadUserByUsername() 함수가 실행됨
//            Authentication authentication = authenticationManager.authenticate(authenticationToken);
//            /**
//             * Authentication에는 로그인한 정보가 담긴다.
//             * session영역에 저장된다
//             */
//            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
//            // 이게 나왔다는 건 인증이 정상적으로 완료되었다는 뜻 => 로그인 완료!
//            System.out.println(principalDetails.getUsername());
//
//            return authentication;
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//
//        }
//        // 2. 정상인지 로그인 시도
//        // 여기서 authenticationManager로 로그인 시도를 하게 되면, PrincipalDetailsService가 호출된다
//        // loadUserByUserName이 자동으로 실행되는 것이다.
//        // 3. PrincipalDetails를 세션에 담고 >> 얘를 세션에 안 담으면 권한관리를 위해서임
//
//        // 4. JWT토큰을 담아서 응답해주면 됨.
//
//
//    }
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//
//}

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.yezi.secretgarden.auth.PrincipalDetails;
import com.yezi.secretgarden.domain.User;

import com.yezi.secretgarden.domain.UserRequestDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Date;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

    private final AuthenticationManager authenticationManager;

    // Authentication 객체 만들어서 리턴 => 의존 : AuthenticationManager
    // 인증 요청시에 실행되는 함수 => /login
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        System.out.println("JwtAuthenticationFilter : 진입");

        // request에 있는 username과 password를 파싱해서 자바 Object로 받기
        ObjectMapper om = new ObjectMapper(); // json 데이터를 파싱해줌
        UserRequestDto userRequestDto = null;
        try {
            // stream안에 username이랑 password가 다 담겨있음
            userRequestDto = om.readValue(request.getInputStream(), UserRequestDto.class);
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
                    authenticationManager.authenticate(authenticationToken);
            PrincipalDetails principalDetailis = (PrincipalDetails) authentication.getPrincipal();
            System.out.println("Authentication : "+principalDetailis.getUser().getUsername()); // 로그인이 정상적으로 되었다
            // authentication 객체가 session 영역에 저장됨 > 그 방법이 return 하는 것
            // 리턴의 이유는 권한 관리를 security 대신 해주므로 편하라고 하는 것
            // 굳이 JWT를 사용하면서 세션을 만들 이유는 없지만 권한처리때문에 세션에 넣어준다.


            return authentication;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;


    }

    // attemptAuthentication 실행 후 인증이 정상적으로 되었으면 successfulAuthentication 실행
    // JWT 토큰을 만들어서 request 요청한 사용자에게 JWT 토큰을 response해주면 됨
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        PrincipalDetails principalDetailis = (PrincipalDetails) authResult.getPrincipal();
        String jwtToken = JWT.create() // com.auth... 걸어놔서 사용 가능
                .withSubject("secret garden token")
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*30))
                .withClaim("id",principalDetailis.getUser().getUsername())
                .withClaim("name",principalDetailis.getUser().getName())
                .sign(Algorithm.HMAC512("secretgarden")).toString();
        
        response.addHeader("Authrorization", "Bearer "+jwtToken); //Bearerd에서 한 칸 띄워야 한다

        System.out.println("successful Authentication : 실행 완료!");
        super.successfulAuthentication(request, response, chain, authResult);
    }
}

/*
    JWT 토큰을 생성
    클라이언트쪽으로 JWT 토큰을 응답
    요청할 때마다 JWT 토큰을 가지고 요청
    서버는 JWT 토큰이 유효한지를 판단 >> 필터를 만들어야 함

 */
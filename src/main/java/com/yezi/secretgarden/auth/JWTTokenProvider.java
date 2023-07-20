package com.yezi.secretgarden.auth;

import antlr.Token;
import com.yezi.secretgarden.domain.TokenInfo;
import com.yezi.secretgarden.domain.User;
import com.yezi.secretgarden.service.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
/**
 * 토큰을 만들고 분석하는 클래스 >
 */
@RequiredArgsConstructor
public class JWTTokenProvider {
    /**
     * JWT 설정
     * - 데이터를 받아서 토큰을 만들거나 분석하는 기능
     * - 사용자의 요청을 검증(filter, validate)하는 기능
     */

    // HTTP 헤더에 담을 키값
    public static String httpKeyValue = "Authorization";
    @Value("${jwt.secret}")
    private String secretKey;
    // 토큰 유효시간 > 60분
    private final long TIME_OUT = 60*60*1000L;


    private final UserDetailsService userDetailsService;

    // 객체 초기화. secretKey를 BASE64로 인코딩
    // 의존성 주입이 이루어진 후로 초기화시켜주는 어노테이션
    @PostConstruct
    protected void secretKeyEncoding() {
        secretKey=Base64.getEncoder().encodeToString(secretKey.getBytes());

    }

    // JWT 토큰 생성 > 사용자의 로그인 행위가 올바르면 토큰값을 전달해주는 메서드
    public String publishToken(String userPk, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(userPk); // JWT Payload에 저장되는 정보 단위. 여기서 user를 식별하는 값을 넣는다.
        claims.put("roles",roles);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now)  // 토큰 발행 시간
                .setExpiration(new Date(now.getTime()+TIME_OUT)) // 만료 시간
                .signWith(SignatureAlgorithm.HS256,secretKey).compact(); // 사용할 암호화 알고리즘과 signature에 들어갈 secret key 값 세팅

    }
    // JWT 토큰에서 인증정보 조회 >> 사용자가 로그인이 된 상태에서 권한이 필요한 페이지로 이동할 때 동작하는 메서드
    public Authentication authentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.userInfoCheck(token));
        log.info(token);
        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
    }
    // 토큰의 유효성 + 만료일자 확인 >> 사용자가 로그인이 된 상태에서 권한이 필요한 페이지로 이동할 때 동작하는 메서드
    public boolean tokenCheck(String token) {
        try {
            Jws<Claims>  claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            log.info(claims.toString());
           return !claims.getBody().getExpiration().before(new Date());

        }catch(Exception e) {
            log.warn(e.getMessage());
            return false;
        }
    }
    // 토큰에서 회원정보 추출
    public String userInfoCheck(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJwt(token).getBody().getSubject();
    }



}

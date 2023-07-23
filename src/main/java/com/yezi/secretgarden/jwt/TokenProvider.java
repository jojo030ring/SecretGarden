package com.yezi.secretgarden.jwt;

import com.yezi.secretgarden.auth.PrincipalDetails;
import com.yezi.secretgarden.service.LoggerService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider {
    @Autowired
    private LoggerService loggerService;
    private static String AUTHORITIES_KEY = "auth";
    private final String secret;
    private Key key;
    private final long tokenValidExp;

    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
        this.secret = secret;
        this.tokenValidExp = tokenValidityInSeconds * 1000;
    }
    // 빈이 생성되고 주입을 받은 후에 secret 값을 base64 decode해서 key변수에 할당하기 위함
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        // token의 expire 시간을 설정
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidExp);
        return Jwts.builder()
                .setSubject(authentication.getName()) // 유저 아이디 아님. getPrincipal이 아이디임
                .claim(AUTHORITIES_KEY,authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();




    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        Collection<? extends GrantedAuthority> authorities
                = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

//        PrincipalDetails principalDetails = new PrincipalDetails();
//        return new UsernamePasswordAuthenticationToken(principalDetails, token, authorities);
        return null;

    }
        // 토큰의 유효성 검증을 수행
        public boolean validateToken(String token) {
            try {
                Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
                return true;
            } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {

                loggerService.infoLoggerTest("잘못된 JWT 서명입니다.");
            } catch (ExpiredJwtException e) {

                loggerService.infoLoggerTest("만료된 JWT 토큰입니다.");
            } catch (UnsupportedJwtException e) {

                loggerService.infoLoggerTest("지원되지 않는 JWT 토큰입니다.");
            } catch (IllegalArgumentException e) {

                loggerService.infoLoggerTest("JWT 토큰이 잘못되었습니다.");
            }
            return false;
    }


}

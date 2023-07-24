package com.yezi.secretgarden.jwt;

import com.yezi.secretgarden.auth.PrincipalDetailService;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TokenProvider {
    @Autowired
    private LoggerService loggerService;
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.token-validity-in-seconds}")
    private long tokenValidTime;
    @Autowired
    private PrincipalDetailService principalDetailService;
    public String createToken(String username, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(username); // JWT payload에 저장되는 정보 단위
        claims.put("roles", roles); // 정보는 key-value 쌍으로 저장
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+tokenValidTime)) // set Expire time
                .signWith(getSigninKey(secretKey)) // signature에 들어갈 secret 값 세팅
                .compact();
    }

    private Key getSigninKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Request의 Header에서 token 값을 가져옵니다. "Token" : "TOKEN값'
    public String resolveToken(HttpServletRequest request) {
        String token = null;
        Cookie cookie = WebUtils.getCookie(request, "Token");
        if(cookie != null) token = URLDecoder.decode(cookie.getValue(),StandardCharsets.UTF_8);
        return token;
    }
    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        String id = getUserId(token);
        PrincipalDetails principalDetails = (PrincipalDetails) principalDetailService.loadUserByUsername(id);
        return new UsernamePasswordAuthenticationToken(principalDetails, "", principalDetails.getAuthorities());
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validate(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(getSigninKey(secretKey)).build().parseClaimsJws(jwtToken);
            System.out.println(claims );
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    // JWT 토큰에서 회원 정보 추출
    public String getUserId(String token) {
        return (String)Jwts.parserBuilder().setSigningKey(getSigninKey(secretKey)).build().parseClaimsJws(token).getBody().getSubject();
    }
    // 토큰의 유효성 검증을 수행
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigninKey(secretKey)).build().parseClaimsJws(token);
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

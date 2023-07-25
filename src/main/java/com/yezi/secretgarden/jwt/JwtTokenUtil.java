package com.yezi.secretgarden.jwt;

import com.yezi.secretgarden.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
@Component
public class JwtTokenUtil {
    // JWT Token 발급
    private final String secretKey="a2FyaW10b2thcmltdG9rYXJpbXRva2FyaW10b2thcmltdG9rYXJpbXRva2FyaW10b2thcmltdG9rYXJpbXRva2FyaW10b2thcmltdG9rYXJpbXRva2FyaW10b2thcmltdG9rYXJpbXRva2FyaW10b2thcmltdG9rYXJpbXRva2FyaW10b2thcmltdG9rYXJpbQ";
    private final Long exp = 86400*1000L;
    private final Key key;
    public JwtTokenUtil() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
    public String createToken(String loginId, User user) {
        // Claim = Jwt Token에 들어갈 정보
        // Claim에 loginId를 넣어 줌으로써 나중에 loginId를 꺼낼 수 있음
        StringBuilder sb = new StringBuilder();
        Claims claims = Jwts.claims();
        claims.put("id", loginId);
        claims.put("auth",user.getRoles());
        sb.append("Bearer ");
        sb.append(Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + exp))
                .signWith(key)
                .compact());
        System.out.println("generated : "+sb.toString());
        return sb.toString();
    }

    // 쿠키에서 jwt(Bearer )를 추출하는 메서드
    public String getJwtFromCookie(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request,"token");
        String jwt = (cookie == null) ? null : URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
        return jwt;
    }
    // 타임리프를 위한 메서드 - 로그인 처리
    public String getLoginId(HttpServletRequest request) {
        String token = getJwtFromCookie(request); // Bearer + jwt
        System.out.println("token = " + token);
        if(token != null) {
            token = token.replace("Bearer ", "");
            return extractClaims(token).get("id",String.class);
        } else
            return null;

    }


    // Claims에서 loginId 꺼내기
    public String getLoginId(String token) {
        return extractClaims(token).get("id").toString();
    }

    // 밝급된 Token이 만료 시간이 지났는지 체크
    public boolean isExpired(String token) {
        Date expiredDate = extractClaims(token).getExpiration();
        // Token의 만료 날짜가 지금보다 이전인지 check
        return expiredDate.before(new Date());
    }

    // SecretKey를 사용해 Token Parsing
    private Claims extractClaims(String token) {
        System.out.println(token);
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }


}

package com.yezi.secretgarden.jwt;

import com.yezi.secretgarden.domain.User;
import com.yezi.secretgarden.service.LoggerService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
@Component
public class JwtTokenUtil {
    // JWT Token 발급
    private final String secretKey="a2FyaW10b2thcmltdG9rYXJpbXRva2FyaW10b2thcmltdG9rYXJpbXRva2FyaW10b2thcmltdG9rYXJpbXRva2FyaW10b2thcmltdG9rYXJpbXRva2FyaW10b2thcmltdG9rYXJpbXRva2FyaW10b2thcmltdG9rYXJpbXRva2FyaW10b2thcmltdG9rYXJpbQ";
    private final Long exp = 500*1000L;
    private final Key key;
    private LoggerService loggerService = new LoggerService();

    // Base64로 인코딩 된 비밀키를 hmacsha 방식으로 말아준다.
    public JwtTokenUtil() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
    public String createToken(String loginId, String auth) {
        // Claim = Jwt Token에 들어갈 정보
        // Claim에 loginId를 넣어 줌으로써 나중에 loginId를 꺼낼 수 있음
        StringBuilder sb = new StringBuilder();

        // payload에 실을 정보인 Claim객체를 생성하고, id(springsecurity에서는 username)와 auth를 할당한다.
        Claims claims = Jwts.claims();
        claims.put("id", loginId);
        claims.put("auth",auth);

        // jwt토큰 발행시 관행처럼 여겨지는 'Bearer '를 앞에 붙여준다.
        sb.append("Bearer ");

        sb.append(Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + exp)) // 만료 기간은 1일로 잡는다.
                .signWith(key) // 앞서 말아준 비밀키로 시그니처를 생성한다
                .compact());   // url-safe한 형태로 발급된 jwt 토큰을 문자열로 반환한다.

        loggerService.infoLoggerTest("JWTTokenUtil > generated Token : "+sb.toString());
        return sb.toString();
    }

    // Bearer << 여기 들어갔던 공백때문에 URL인코딩 했던 token을 다시 변환해준다.
    public String decodeFromCookieToJWT(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request,"token");
        String jwt = (cookie == null) ? null : URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
        return jwt;
    }
    // 타임리프를 위한 메서드 - 토큰으로부터 Bearer를 제외한 퓨어한 jwt를 얻어낸 뒤, id를 뽑아내는 메서드
    public String getLoginId(HttpServletRequest request) {
        String token = decodeFromCookieToJWT(request); // Bearer + jwt
        loggerService.infoLoggerTest("JWTTokenUtil > userId :" + token);
        if(isJWTForm(token)) {
            token = getPureJWT(token);
            return getLoginId(token);
        } else {
            return null;
        }

    }

    public String getLoginId(String token) {
        if(token != null && validateToken(token)) {
            return extractClaims(token).get("id").toString();
        } else {
            return null;
        }

    }
    public String getAuth(String token) {
        if(token != null && validateToken(token)) {
            return extractClaims(token).get("auth").toString();
        } else {
            return null;
        }

    }


    // JWTAuthorizationFilter에서 사용되는 메서드
    // Bearer 로 시작하는지, null인지를 검증하게 된다.
    public String getPureJWT(String token) {
        if(isJWTForm(token)) {

            return token.replace("Bearer ","");
        } else {
            return null;
        }
    }
    public boolean isJWTForm(String token) {
        return token != null && token.startsWith("Bearer ");
    }
    // SecretKey를 사용해 Token Parsing
    // 이때 쿠키를 변조하면 에러를 내게 된다.
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
    public boolean validateToken(String token) {

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            loggerService.errorLoggerTest("잘못된 JWT 서명입니다.");
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
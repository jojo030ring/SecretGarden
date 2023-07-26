package com.yezi.secretgarden.jwt_temp;

import com.yezi.secretgarden.domain.User;
import com.yezi.secretgarden.service.LoggerService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private LoggerService loggerService;

    // Base64로 인코딩 된 비밀키를 hmacsha 방식으로 말아준다.
    public JwtTokenUtil() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
    public String createToken(String loginId, User user) {
        // Claim = Jwt Token에 들어갈 정보
        // Claim에 loginId를 넣어 줌으로써 나중에 loginId를 꺼낼 수 있음
        StringBuilder sb = new StringBuilder();

        // payload에 실을 정보인 Claim객체를 생성하고, id(springsecurity에서는 username)와 auth를 할당한다.
        Claims claims = Jwts.claims();
        claims.put("id", loginId);
        claims.put("auth",user.getRoles());

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

    // 쿠키에서 jwt(Bearer jwt토큰)를 추출하는 메서드
    public String getJwtFromCookie(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request,"token");
        String jwt = (cookie == null) ? null : URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
        return jwt;
    }
    // 타임리프를 위한 메서드 - 토큰으로부터 Bearer를 제외한 퓨어한 jwt를 얻어낸 뒤, id를 뽑아내는 메서드
    public String getLoginId(HttpServletRequest request) {
        String token = getJwtFromCookie(request); // Bearer + jwt
        token = getPureJwt(request);
        if (token != null) {
            token = token.replace("Bearer ", "");
            return extractClaims(token).get("id", String.class);
        } else
            return null;

    }
    // 순수한 jwt를 얻는 메서드
    public String getPureJwt(HttpServletRequest request) {
        String token = getJwtFromCookie(request); // Bearer + jwt
        if(token != null) {
            token = token.replace("Bearer ", "");
            return token;
        } else {
            return null;
        }
    }
    public String getAuth(String token, String target) {
        if (token != null) {
            return extractClaims(token).get("Auth", String.class);
        } else
            return null;

    }


    // JWTAuthorizationFilter에서 사용되는 메서드
    // Bearer 로 시작하는지, null인지를 검증하게 된다.
    public String getLoginId(String token) {

        return extractClaims(token).get("id").toString();

    }

    // 발급된 Token이 만료 시간이 지났는지 체크 - 예외처리
    public boolean isExpired(String token) {
        // exp로 지정된 expiredDate를 받아온다.
        Date expiredDate = extractClaims(token).getExpiration();
        // Token의 만료 날짜가 지금보다 이전인지 check하여 이전이면 true, 이후면 false를 리턴한다.
        return expiredDate.before(new Date());
    }

    // SecretKey를 사용해 Token Parsing
    // 이때 쿠키를 변조하면 에러를 내게 된다.
    private Claims extractClaims(String token) {
        System.out.println(token);
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }


}


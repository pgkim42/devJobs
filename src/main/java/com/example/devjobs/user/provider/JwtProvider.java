package com.example.devjobs.user.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${secret-key}")
    private String secretKey;

    // JWT 생성 메소드
    public String create(String userId) {
        Date expiredDate = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));  // 1시간 유효기간
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setSubject(userId) // userId는 JWT의 subject로 설정
                .setIssuedAt(new Date()) // 발급 시간
                .setExpiration(expiredDate) // 만료 시간
                .compact();
    }

    // JWT 검증 메소드
    public String validate(String jwt) {
        String subject = null;
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        try {
            subject = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)  // JWT를 파싱하여 검증
                    .getBody()
                    .getSubject();  // JWT의 subject 값을 가져옴
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }

        return subject;
    }

    // JWT에서 클레임을 가져오는 메소드 (예: 만료 시간)
    public Claims getClaims(String jwt) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)  // JWT 파싱
                    .getBody();  // 클레임 반환
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    // JWT의 만료 시간을 초 단위로 반환하는 메소드
    public long getExpirationTime(String jwt) {
        Claims claims = getClaims(jwt);
        if (claims != null) {
            Date expirationDate = claims.getExpiration();
            return (expirationDate.getTime() - System.currentTimeMillis()) / 1000;  // 만료 시간을 초 단위로 계산
        }
        return -1;  // 만약 클레임이 없으면 -1 반환
    }
}

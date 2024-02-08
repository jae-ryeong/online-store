package com.project.onlinestore.jwt.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtils {

    @Value("${jwt.secret-key}")
    private String key;
    @Value("${jwt.expired-time-ms}")
    private Long expireTime;

    public String generateToken(String userName) {
        Claims claims = Jwts.claims();
        claims.put("userName", userName);

        return Jwts.builder()
                .setSubject(userName)
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getKey(key), SignatureAlgorithm.HS256)
                .compact();
    }

    // 토근의 유효성 검증 수행
    public static boolean validationToken(String token, String key) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getKey(key))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.");
        } catch (SecurityException | MalformedJwtException e) { // MalformedJwtException: 전달되는 토큰의 값이 유효하지 않을 때 발생하는 예외
            log.error("잘못된 JWT 서명입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public static String getUserName(String token, String key) {
        return extractClaims(token, key).get("userName", String.class);
    }

    private static Key getKey(String key) {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private static Claims extractClaims(String token, String key) {
        return Jwts.parserBuilder().setSigningKey(getKey(key))
                .build().parseClaimsJws(token).getBody();
    }
}

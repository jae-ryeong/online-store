package com.project.onlinestore.jwt.util;

import com.project.onlinestore.exception.ApplicationException;
import com.project.onlinestore.exception.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtils {

    @Value("${jwt.secret-key}")
    private String key;
    @Value("${jwt.access-expired-time-ms}")
    private Long accessExpireTime;
    @Value("${jwt.refresh-expiration-time}")
    private Long refreshExpireTime;

    private final RedisTemplate<String, String> redisTemplate;

    /*
    1. 로그인을 하면 Access Token 과 Refresh Token을 발급해준다. ( Refresh 토큰은 레디스에 저장)
    2. 클라이언트는 API를 호출할 때마다 발급받은 Access Token을 활용하여 요청을 한다.
    3. 토큰을 사용하던 중, 만료되어 Invalid Token Error가 발생한다면 header에서 Refresh Token을 추출 후 레디스의 Refresh Token과 비교하여 Refresh 토큰이 유효하다면, Access Token을 다시 발급해준다.
    4. Redis에 Refresh Token과 짝을 이루는 Access Token을 새로 발급한 토큰으로 업데이트한다.
    5. 만약, Refresh Token도 만료되었다면, 다시 로그인을 하도록 요청한다.
    6.  만약 사용자가 로그아웃을 하면, refresh token을 삭제하고 사용이 불가하도록 한다.
    */

    public String generateToken(String userName) {
        Claims claims = Jwts.claims();
        claims.put("userName", userName);

        return Jwts.builder()
                .setSubject(userName)
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessExpireTime))
                .signWith(getKey(key), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String userName) {
        Claims claims = Jwts.claims();
        claims.put("userName", userName);

        Date now = new Date();

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshExpireTime))
                .signWith(getKey(key), SignatureAlgorithm.HS256)
                .compact();

        // redis에 저장
        redisTemplate.opsForValue().set(
                userName,
                refreshToken,
                refreshExpireTime,  // key에 대한 만료 타임아웃
                TimeUnit.MILLISECONDS
        );

        return refreshToken;
    }

    // 토근의 유효성 검증 수행
    public boolean validationToken(String token, String key) {
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

    public String getUserName(String token, String key) {
        return extractClaims(token, key).get("userName", String.class);
    }

    public Long getExpiration(String accessToken) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(getKey(key))
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .getExpiration();

        long now = new Date().getTime();

        return (expiration.getTime() - now);
    }

    public void accessTokenBlackList(String accessToken, Long expiration){
        redisTemplate.opsForValue().set(
                accessToken
                , "logout"
                , expiration
                , TimeUnit.MILLISECONDS
        );
    }

    public String getRedisRefreshToken(String userName) {
        return redisTemplate.opsForValue().get(userName);
    }

    public void deleteRefreshToken(String userName){
        redisTemplate.delete(userName);
    }

    private Key getKey(String key) {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractClaims(String token, String key) {
        return Jwts.parserBuilder().setSigningKey(getKey(key))
                .build().parseClaimsJws(token).getBody();
    }

    private void validBlackToken(String accessToken) {
        // Redis 블랙리스트에 올라온 accessToken인지 검증
        if(!redisTemplate.opsForValue().get(accessToken).isEmpty()){
            throw new ApplicationException(ErrorCode.ACCESS_TOKEN_IN_BLACKLIST, null);
        }
    }
}

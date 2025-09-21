package com.kombuchagrande.dailyhabit.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.clock-skew-seconds:0}")
    private long clockSkewSeconds;

    private SecretKey signingKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secret);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException ignore) {
            return Keys.hmacShaKeyFor(secret.getBytes());
        }
    }

    // ===== Issue =====
    public String createAccessToken(Long userId, String role, String tid, long expSeconds) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expSeconds);
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("role", role)
                .claim("tid", tid)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(signingKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String createRefreshToken(Long userId, String tid, long expSeconds) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expSeconds);
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("tid", tid)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(signingKey(), Jwts.SIG.HS256)
                .compact();
    }

    // ===== Parse/Verify =====
    /** 서명+만료 엄격 검증(만료 시 ExpiredJwtException). */
    public Claims parseClaimsStrict(String token) {
        JwtParserBuilder b = Jwts.parser()
                .verifyWith(signingKey());
        if (clockSkewSeconds > 0) {
            b = b.clockSkewSeconds(clockSkewSeconds);
        }
        return b.build().parseSignedClaims(token).getPayload();
    }

    /** 로그아웃 용: 서명 OK면 만료여도 Claims 반환. */
    public Claims parseClaimsAllowExpired(String token) {
        try {
            return parseClaimsStrict(token);
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
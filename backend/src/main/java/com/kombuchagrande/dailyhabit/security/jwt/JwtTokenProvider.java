package com.kombuchagrande.dailyhabit.security.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final SecretKey signingKey;
    private final JwtParser strictJwtParser;

    // ===== Issue =====
    public String createAccessToken(Long userId, String role, String tid, Instant issuedAt ,Instant expiresAt) {
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("role", role)
                .claim("tid", tid)
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey, Jwts.SIG.HS256)
                .compact();
    }

    public String createRefreshToken(Long userId, String tid, Instant issuedAt, Instant expiresAt) {
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("tid", tid)
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey, Jwts.SIG.HS256)
                .compact();
    }

    // ===== Parse/Verify =====
    /** 서명+만료 엄격 검증(만료 시 ExpiredJwtException). */
    public Claims parseClaimsStrict(String token) {
        return strictJwtParser.parseSignedClaims(token).getPayload();
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
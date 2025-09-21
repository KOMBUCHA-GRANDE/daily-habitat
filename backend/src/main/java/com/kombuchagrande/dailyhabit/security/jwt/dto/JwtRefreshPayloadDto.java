package com.kombuchagrande.dailyhabit.security.jwt.dto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

public record JwtRefreshPayloadDto(
        Long userId,
        String tid,
        Instant iat,
        Instant exp
) {
    public JwtRefreshPayloadDto {
        Objects.requireNonNull(userId, "userId");
        if (tid == null || tid.isBlank()) throw new IllegalArgumentException("tid");
        Objects.requireNonNull(iat, "iat");
        Objects.requireNonNull(exp, "exp");
        if (!iat.isBefore(exp)) throw new IllegalArgumentException("iat >= exp");
    }

    public static JwtRefreshPayloadDto fromClaims(Claims c) {
        Long uid = parseUserId(c.getSubject());
        String tid = mustNonBlank(c.get("tid", String.class), "missing_tid");

        Date iatD = c.getIssuedAt();
        Date expD = c.getExpiration();
        if (iatD == null) throw new JwtException("missing_iat");
        if (expD == null) throw new JwtException("missing_exp");

        return new JwtRefreshPayloadDto(
                uid,
                tid,
                iatD.toInstant(),
                expD.toInstant()
        );
    }

    private static Long parseUserId(String sub) {
        if (sub == null || sub.isBlank()) throw new JwtException("missing_sub");
        try { return Long.parseLong(sub); }
        catch (NumberFormatException e) { throw new JwtException("invalid_sub", e); }
    }
    private static String mustNonBlank(String v, String code) {
        if (v == null || v.isBlank()) throw new JwtException(code);
        return v;
    }
}
package com.kombuchagrande.dailyhabit.security.jwt.dto;

import com.kombuchagrande.dailyhabit.entity.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

public record JwtAccessPayloadDto(
        Long userId,
        Role role,
        String tid,
        Instant iat,
        Instant exp
) {
    // 어떤 경로로 만들어도 스키마 불변식 보장
    public JwtAccessPayloadDto {
        Objects.requireNonNull(userId, "userId");
        Objects.requireNonNull(role, "role");
        if (tid == null || tid.isBlank()) throw new IllegalArgumentException("tid");
        Objects.requireNonNull(iat, "iat");
        Objects.requireNonNull(exp, "exp");
        if (!iat.isBefore(exp)) throw new IllegalArgumentException("iat >= exp");
    }

    public static JwtAccessPayloadDto fromClaims(Claims c) {
        Long uid = parseUserId(c.getSubject());
        Role role = parseRole(c.get("role", String.class));
        String tid = mustNonBlank(c.get("tid", String.class), "missing_tid");

        Date iatD = c.getIssuedAt();
        Date expD = c.getExpiration();
        if (iatD == null) throw new JwtException("missing_iat");
        if (expD == null) throw new JwtException("missing_exp");

        return new JwtAccessPayloadDto(
                uid,
                role,
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
    private static Role parseRole(String roleStr) {
        if (roleStr == null || roleStr.isBlank()) throw new JwtException("missing_role");
        try { return Role.valueOf(roleStr); }
        catch (IllegalArgumentException e) { throw new JwtException("invalid_role", e); }
    }
    private static String mustNonBlank(String v, String code) {
        if (v == null || v.isBlank()) throw new JwtException(code);
        return v;
    }
}

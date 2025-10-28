package com.kombuchagrande.dailyhabit.security.jwt.dto;

public record LoginTokenResponse(
        String accessToken,
        String refreshToken,
        long accessTokenExpiresAt,
        boolean isNewUser
) {}

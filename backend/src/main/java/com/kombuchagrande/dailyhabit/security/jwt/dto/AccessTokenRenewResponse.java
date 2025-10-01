package com.kombuchagrande.dailyhabit.security.jwt.dto;

public record AccessTokenRenewResponse(
        String accessToken,
        long accessTokenExpiresAt
) {}
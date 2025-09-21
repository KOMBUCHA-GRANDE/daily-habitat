package com.kombuchagrande.dailyhabit.security.jwt.dto;

public record JwtTokenResponse(
        String accessToken,
        String refreshToken
) {

}


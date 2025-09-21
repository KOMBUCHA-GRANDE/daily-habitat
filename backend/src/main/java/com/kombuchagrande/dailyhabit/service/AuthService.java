package com.kombuchagrande.dailyhabit.service;

import com.kombuchagrande.dailyhabit.security.jwt.JwtService;
import com.kombuchagrande.dailyhabit.security.jwt.dto.JwtTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;

    // 엑세스 재발급
    public JwtTokenResponse refresh(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new IllegalArgumentException("refreshToken is required");
        }
        return jwtService.refreshJwtToken(refreshToken);
    }

    // 현재 기기 로그아웃 (엑시스만, 만료 허용)
    public void logout(String authorizationHeader) {
        String access = resolveBearer(authorizationHeader);
        if (access == null) throw new IllegalArgumentException("access token is required");
        jwtService.logoutCurrentDevice(access);
    }

    // 모든 기기 로그아웃 (엑시스만, 만료허용 + cutoff)
    public void logoutAll(String authorizationHeader) {
        String access = resolveBearer(authorizationHeader);
        if (access == null) throw new IllegalArgumentException("access token is required");
        jwtService.logoutAllDevices(access);
    }

    // util
    private String resolveBearer(String authorization) {
        if (authorization == null) return null;
        return authorization.startsWith("Bearer ") ? authorization.substring(7) : null;
    }
}
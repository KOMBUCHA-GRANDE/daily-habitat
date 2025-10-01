package com.kombuchagrande.dailyhabit.controller;

import com.kombuchagrande.dailyhabit.dto.auth.RefreshRequest;
import com.kombuchagrande.dailyhabit.security.jwt.dto.AccessTokenRenewResponse;
import com.kombuchagrande.dailyhabit.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 액세스 토큰 재발급
    @PostMapping("/token/refresh")
    public ResponseEntity<AccessTokenRenewResponse> refresh(
            @RequestBody @Valid RefreshRequest request
    ) {
        AccessTokenRenewResponse refresh = authService.refresh(request.refreshToken());
        return ResponseEntity.ok(refresh);
    }

    // 현재 기기 로그아웃 Authorization -> 엑시스 토큰 안에 uid 사용하여 리프레시 토큰 삭제 + 엑시스 블렉리스트 (컷오프)
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization
    ) {
        authService.logout(authorization);
        return ResponseEntity.noContent().build();
    }

    // 모든 기기 로그아웃 (컷오프 추가)
    @PostMapping("/logout/all")
    public ResponseEntity<Void> logoutAll(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    ) {
        authService.logoutAll(authorization);
        return ResponseEntity.noContent().build();
    }

}
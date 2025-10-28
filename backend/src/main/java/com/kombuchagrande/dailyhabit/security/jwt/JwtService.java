package com.kombuchagrande.dailyhabit.security.jwt;

import com.kombuchagrande.dailyhabit.entity.User;
import com.kombuchagrande.dailyhabit.entity.enums.Role;
import com.kombuchagrande.dailyhabit.repository.UserRepository;
import com.kombuchagrande.dailyhabit.security.jwt.dto.*;
import com.kombuchagrande.dailyhabit.security.session.RedisTokenStore;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;


@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.access-token-expiration-seconds}")
    private long accessExp;

    @Value("${jwt.refresh-token-expiration-seconds}")
    private long refreshExp;

    private final UserRepository userRepository;
    private final RedisTokenStore redisTokenStore;
    private final JwtTokenProvider jwtTokenProvider;

    // 로그인 성공 시 발급
    @Transactional
    public LoginTokenResponse registerJwtToken(JwtPayloadDto payload, boolean isNewUser) {
        String tid = java.util.UUID.randomUUID().toString();
        Instant now = Instant.now();

        Instant accessExpAt = now.plusSeconds(accessExp);
        Instant refreshExpAt = now.plusSeconds(refreshExp);

        String access = jwtTokenProvider.createAccessToken(payload.userId(), payload.role().name(), tid, now, accessExpAt);
        String refresh = jwtTokenProvider.createRefreshToken(payload.userId(), tid, now, refreshExpAt);

        redisTokenStore.saveRefreshSession(tid, payload.userId(), Duration.ofSeconds(refreshExp));

        return new LoginTokenResponse(
                access,
                refresh,
                accessExpAt.toEpochMilli(),
                isNewUser
        );
    }

    // 재발급
    @Transactional
    public AccessTokenRenewResponse refreshJwtToken(String refreshToken) {
        Claims claims = jwtTokenProvider.parseClaimsStrict(refreshToken);
        JwtRefreshPayloadDto jwtRefreshPayloadDto = JwtRefreshPayloadDto.fromClaims(claims);

        if (!redisTokenStore.isRefreshSessionActive(jwtRefreshPayloadDto.tid(), jwtRefreshPayloadDto.userId())) {
            throw new JwtException("refresh_revoked");
        }

        Role latestRole = userRepository.findById(jwtRefreshPayloadDto.userId())
                .map(User::getRole)
                .orElseThrow(() -> new JwtException("user_not_found"));

        Instant now = Instant.now();
        Instant accessExpAt = now.plusSeconds(accessExp);

        String newAccess = jwtTokenProvider.createAccessToken(jwtRefreshPayloadDto.userId(), latestRole.name(), jwtRefreshPayloadDto.tid(), now, accessExpAt);
        return new AccessTokenRenewResponse(newAccess, accessExpAt.toEpochMilli());
    }

    // 단일 기기 로그아웃
    @Transactional
    public void logoutCurrentDevice(String accessToken) {
        Claims claims = jwtTokenProvider.parseClaimsAllowExpired(accessToken);
        JwtAccessPayloadDto accessPayloadDto = JwtAccessPayloadDto.fromClaims(claims);

        redisTokenStore.deleteRefreshSession(accessPayloadDto.tid(), accessPayloadDto.userId());
        redisTokenStore.removeIndex(accessPayloadDto.userId(), accessPayloadDto.tid());

        long now = Instant.now().getEpochSecond();
        long left = Math.max(0L, accessPayloadDto.exp().getEpochSecond() - now);
        long skewSeconds = 10L;

        Duration ttl = Duration.ofSeconds(Math.max(1L, left + skewSeconds));
        redisTokenStore.setDeviceCutoff(accessPayloadDto.tid(), now, ttl);
    }

    // 전체 기기 로그아웃
    @Transactional
    public void logoutAllDevices(String accessToken) {
        Claims claims = jwtTokenProvider.parseClaimsAllowExpired(accessToken);
        JwtAccessPayloadDto accessPayloadDto = JwtAccessPayloadDto.fromClaims(claims);

        redisTokenStore.deleteAllSessionsByUser(accessPayloadDto.userId());
        redisTokenStore.setUserCutoff(accessPayloadDto.userId(), Instant.now().getEpochSecond());
    }

    // 보호 API에서 컷오프 확인
    public boolean isAfterUserCutoff(Long uid, Instant iat) {
        Long cutoff = redisTokenStore.getUserCutoff(uid);
        return cutoff != null && iat.isBefore(Instant.ofEpochSecond(cutoff));
    }

    public boolean isAfterDeviceCutoff(String tid, Instant iat) {
        Long cutoff = redisTokenStore.getDeviceCutoff(tid);
        return cutoff != null && iat.isBefore(Instant.ofEpochSecond(cutoff));
    }

}
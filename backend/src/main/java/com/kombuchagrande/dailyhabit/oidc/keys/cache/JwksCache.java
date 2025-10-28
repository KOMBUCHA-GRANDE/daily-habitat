package com.kombuchagrande.dailyhabit.oidc.keys.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kombuchagrande.dailyhabit.oidc.keys.fetch.JwksFetcher;
import com.kombuchagrande.dailyhabit.oidc.keys.dto.JwksSetDto;
import com.kombuchagrande.dailyhabit.oidc.keys.fetch.RestJwksFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwksCache {

    private final RestJwksFetcher fetcher;      // 원격에서 JWKS JSON 가져오는 컴포넌트
    private final ObjectMapper objectMapper;

    // 캐시 미스일 때만 원격 호출
    @Cacheable(cacheNames = "jwks", key = "#jwksUri", cacheManager = "oidcCacheManager")
    public JwksSetDto getSet(String jwksUri) {
        return fetchAndParse(jwksUri);
    }

    // 강제 리프레시(항상 원격) 후 캐시에 덮어쓰기
    @CachePut(cacheNames = "jwks", key = "#jwksUri", cacheManager = "oidcCacheManager")
    public JwksSetDto refreshSet(String jwksUri) {
        return fetchAndParse(jwksUri);
    }


    private JwksSetDto fetchAndParse(String jwksUri) {
        try {
            String json = fetcher.fetchJson(jwksUri);
            JwksSetDto jwksSet = objectMapper.readValue(json, JwksSetDto.class);
            if (jwksSet == null || jwksSet.keys() == null || jwksSet.keys().isEmpty()) {
                throw new IllegalStateException("Empty JWKS keys");
            }
            return jwksSet;
        } catch (Exception e) {
            throw new IllegalStateException("Invalid JWKS JSON from " + jwksUri, e);
        }
    }

}
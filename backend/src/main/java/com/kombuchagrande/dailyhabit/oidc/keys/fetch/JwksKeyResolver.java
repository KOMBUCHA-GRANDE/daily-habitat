package com.kombuchagrande.dailyhabit.oidc.keys.fetch;

import com.kombuchagrande.dailyhabit.oidc.keys.cache.JwksCache;
import com.kombuchagrande.dailyhabit.oidc.keys.dto.JwksSetDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwksKeyResolver {

    private final JwksCache jwksCache;

    public JwksSetDto getJwksSet(String jwksUri) {
        return jwksCache.getSet(jwksUri);
    }

    public JwksSetDto refreshJwksSet(String jwksUri) {
        return jwksCache.refreshSet(jwksUri);
    }
}
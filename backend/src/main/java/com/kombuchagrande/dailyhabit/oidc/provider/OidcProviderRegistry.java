package com.kombuchagrande.dailyhabit.oidc.provider;

import com.kombuchagrande.dailyhabit.entity.enums.ProviderType;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

@Component
public class OidcProviderRegistry {

    private final Map<ProviderType, OidcProperties.Provider> providersByType;

    public OidcProviderRegistry(OidcProperties properties) {
        Map<ProviderType, OidcProperties.Provider> providerMap = new EnumMap<>(ProviderType.class);
        providerMap.putAll(properties.providers());
        this.providersByType = Collections.unmodifiableMap(providerMap);
    }

    public OidcProperties.Provider get(ProviderType provider) {
        return Optional.ofNullable(providersByType.get(provider))
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 제공자입니다. provider: " + provider));
    }
}
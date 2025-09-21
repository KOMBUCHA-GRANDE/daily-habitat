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

//    public OidcProviderRegistry(OidcProperties properties) {
//        this.providersByType = Collections.unmodifiableMap(
//                new EnumMap<>(properties.providers()) // Map 그대로 복사해 생성 -> Java 9 이상 좋은디?
                    //이 방식은 생성자(new EnumMap<>(Map))는 입력 맵이 비어있으면 IllegalArgumentException 발생
//        );
//    }

    public OidcProperties.Provider get(ProviderType provider) {
        return Optional.ofNullable(providersByType.get(provider))
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 제공자입니다. provider: " + provider));
    }
}
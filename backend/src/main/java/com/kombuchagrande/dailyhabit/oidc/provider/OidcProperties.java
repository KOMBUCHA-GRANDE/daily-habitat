package com.kombuchagrande.dailyhabit.oidc.provider;

import com.kombuchagrande.dailyhabit.entity.enums.ProviderType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.*;

@Validated
@ConfigurationProperties(prefix = "security.oidc")
public record OidcProperties(
        @NotEmpty Map<ProviderType, Provider> providers
) {
    public record Provider(
            @NotBlank String issuer,
            @NotBlank String clientId,
            @NotBlank String jwksUri,
            Set<AllowedAlg> algAllowlist,   // 없으면 기본 RS256
            Duration clockSkew              // 없으면 기본 60s
    ) {
        public Provider {
            if (algAllowlist == null || algAllowlist.isEmpty()) {
                algAllowlist = Collections.unmodifiableSet(EnumSet.of(AllowedAlg.RS256));
            } else {
                // 방어적 복사 + 불변화
                algAllowlist = Collections.unmodifiableSet(EnumSet.copyOf(algAllowlist));
            }
            if (clockSkew == null) {
                clockSkew = Duration.ofSeconds(60);
            }
        }
    }
}
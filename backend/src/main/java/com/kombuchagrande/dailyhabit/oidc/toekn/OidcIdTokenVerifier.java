package com.kombuchagrande.dailyhabit.oidc.toekn;

import com.kombuchagrande.dailyhabit.entity.enums.ProviderType;
import com.kombuchagrande.dailyhabit.exception.KidNotFoundException;
import com.kombuchagrande.dailyhabit.oidc.keys.dto.JwksSetDto;
import com.kombuchagrande.dailyhabit.oidc.dto.VerifiedOidc;
import com.kombuchagrande.dailyhabit.oidc.keys.fetch.JwksKeyResolver;
import com.kombuchagrande.dailyhabit.oidc.provider.OidcProperties;
import com.kombuchagrande.dailyhabit.oidc.toekn.header.JwtHeaderDto;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.PublicKey;

@Component
@RequiredArgsConstructor
public class OidcIdTokenVerifier {
    private final JwksKeyResolver keyResolver;
    private final OidcIdTokenValidator validation;

    public VerifiedOidc verify(String idToken,
                               OidcProperties.Provider provider,
                               ProviderType providerType) {

        // 헤더 선검증(alg allowlist, kid 존재)
        JwtHeaderDto header = validation.precheckHeader(idToken, provider.algAllowlist());

        // JWKS 세트(캐시) 가져오기
        JwksSetDto jwksSet = keyResolver.getJwksSet(provider.jwksUri());

        // 공개키 목록 + kid로 PublicKey 생성
        //PublicKey publicKey = validation.publicKeyFromJwks(jwksSet, header.kid());

        PublicKey publicKey;
        try {
            publicKey = validation.publicKeyFromJwks(jwksSet, header.kid());
        } catch (KidNotFoundException e) {
            // kid 미스면 1회만 강제 리프레시 후 재시도
            JwksSetDto refreshed = keyResolver.refreshJwksSet(provider.jwksUri());
            publicKey = validation.publicKeyFromJwks(refreshed, header.kid()); // 재시도 후 없으면 예외 전파
        }

        // 서명/시간 검증 + Claims 파싱
        Claims claims = validation.verifySignatureAndParse(idToken, publicKey, provider.clockSkew());

        // 표준 클레임 검증(iss/aud)
        validation.validateStandardClaims(claims, provider.issuer(), provider.clientId());

        // 공급자별 규칙이 있으면 여기서 호출하여 검증
        // validation.validateProviderSpecific(providerType, claims);

        return new VerifiedOidc(providerType, claims.getSubject());
    }
}
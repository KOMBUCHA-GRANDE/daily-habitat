package com.kombuchagrande.dailyhabit.oidc;


import com.kombuchagrande.dailyhabit.oidc.dto.OidcVerificationCommand;
import com.kombuchagrande.dailyhabit.oidc.dto.VerifiedOidc;
import com.kombuchagrande.dailyhabit.config.oidc.OidcProperties;
import com.kombuchagrande.dailyhabit.oidc.provider.OidcProviderRegistry;
import com.kombuchagrande.dailyhabit.oidc.toekn.OidcIdTokenVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OidcAuthService {
    private final OidcProviderRegistry registry;
    private final OidcIdTokenVerifier idTokenVerifier;

    // Todo providerType 맞지 않아서 오류 발생하는 경우 -> 전역 예외처리 추가하기

    public VerifiedOidc verify(OidcVerificationCommand request) {
        OidcProperties.Provider providerProps = registry.get(request.provider());
        return idTokenVerifier.verify(request.idToken(), providerProps, request.provider());
    }
}
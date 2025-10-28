package com.kombuchagrande.dailyhabit.security.auth;

import com.kombuchagrande.dailyhabit.entity.enums.ProviderType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class OidcLoginAuthenticationToken extends AbstractAuthenticationToken {

    private final ProviderType provider;
    private final String idToken;
    private final Object principal; // 성공 시 CustomDetailsImpl

    // 로그인 시도 단계
    public OidcLoginAuthenticationToken(ProviderType provider, String idToken) {
        super(null);
        this.provider = provider;
        this.idToken = idToken;
        this.principal = null;
        setAuthenticated(false);
    }

    // 인증 성공 단계
    public OidcLoginAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.provider = null;
        this.idToken = null;
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() { return ""; }

    @Override
    public Object getPrincipal() { return principal; }

    public ProviderType getProvider() { return provider; }

    public String getIdToken() { return idToken; }
}
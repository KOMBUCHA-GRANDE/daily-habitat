package com.kombuchagrande.dailyhabit.security.auth;

import com.kombuchagrande.dailyhabit.oidc.OidcAuthService;
import com.kombuchagrande.dailyhabit.oidc.dto.OidcVerificationCommand;
import com.kombuchagrande.dailyhabit.oidc.dto.VerifiedOidc;
import com.kombuchagrande.dailyhabit.security.jwt.dto.LoadOrCreateResult;
import com.kombuchagrande.dailyhabit.security.userdetails.OidcUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OidcAuthenticationProvider implements AuthenticationProvider {

    private final OidcAuthService oidcAuthService;
    private final OidcUserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OidcLoginAuthenticationToken token = (OidcLoginAuthenticationToken) authentication;

        VerifiedOidc verifiedOidc = oidcAuthService.verify(
                new OidcVerificationCommand(token.getProvider(), token.getIdToken()));

        LoadOrCreateResult result = userDetailsService.loadOrCreate(verifiedOidc);

        OidcLoginAuthenticationToken oidcLoginAuthenticationToken
                = new OidcLoginAuthenticationToken(result.userDetails(), result.userDetails().getAuthorities());
        oidcLoginAuthenticationToken.setDetails(result);

        return oidcLoginAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OidcLoginAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
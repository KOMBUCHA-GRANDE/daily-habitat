package com.kombuchagrande.dailyhabit.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kombuchagrande.dailyhabit.oidc.dto.OidcVerificationCommand;
import com.kombuchagrande.dailyhabit.security.auth.OidcLoginAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class OidcLoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    public OidcLoginAuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher("/auth/login", "POST"));
        setAuthenticationManager(authenticationManager);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response) throws IOException {

        if (request.getContentType() == null ||
                !request.getContentType().startsWith(MediaType.APPLICATION_JSON_VALUE)) {
            throw new AuthenticationServiceException("Content-Type must be application/json");
        }

        OidcVerificationCommand body;
        try {
            body = objectMapper.readValue(request.getInputStream(), OidcVerificationCommand.class);
        } catch (Exception e) {
            throw new AuthenticationServiceException("Malformed JSON"); // 400
        }

        if (body == null || body.provider() == null || !StringUtils.hasText(body.idToken())) {
            throw new AuthenticationServiceException("Missing required fields: provider, idToken"); //400
        }

        OidcLoginAuthenticationToken token = new OidcLoginAuthenticationToken(body.provider(), body.idToken());

        return this.getAuthenticationManager().authenticate(token);
    }
}
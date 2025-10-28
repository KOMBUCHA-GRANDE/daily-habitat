package com.kombuchagrande.dailyhabit.security.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kombuchagrande.dailyhabit.security.jwt.dto.JwtPayloadDto;
import com.kombuchagrande.dailyhabit.security.jwt.JwtService;
import com.kombuchagrande.dailyhabit.security.jwt.dto.LoadOrCreateResult;
import com.kombuchagrande.dailyhabit.security.jwt.dto.LoginTokenResponse;
import com.kombuchagrande.dailyhabit.security.userdetails.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OidcLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        JwtPayloadDto payload = principal.getPayload();

        boolean isNewUser = false;
        Object details = authentication.getDetails();
        if (details instanceof LoadOrCreateResult result) {
            isNewUser = result.isNewUser();
        }

        // Access/Refresh 발급 (레디스에 refresh tid 저장)
        LoginTokenResponse tokens = jwtService.registerJwtToken(payload, isNewUser);

        LoginTokenResponse body = jwtService.registerJwtToken(payload, isNewUser);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        objectMapper.writeValue(response.getOutputStream(), body); //직렬화
    }
}
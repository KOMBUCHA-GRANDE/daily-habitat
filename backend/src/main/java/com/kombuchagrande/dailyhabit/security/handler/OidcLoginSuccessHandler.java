package com.kombuchagrande.dailyhabit.security.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kombuchagrande.dailyhabit.security.jwt.dto.JwtPayloadDto;
import com.kombuchagrande.dailyhabit.security.jwt.JwtService;
import com.kombuchagrande.dailyhabit.security.jwt.dto.JwtTokenResponse;
import com.kombuchagrande.dailyhabit.security.userdetails.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

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

        // Access/Refresh 발급 (레디스에 refresh tid 저장)
        JwtTokenResponse tokens = jwtService.registerJwtToken(payload);


        //여기서 왜 userId, role 같이 전달하지?? 이거 고민해보자
        // 그냥 토큰 디코딩 없이 즉시 ui 구성 가능? -> /me 로 다시 불러올텐데 안넣어도 될듯
        Map<String, Object> body = new LinkedHashMap<>();
        //body.put("userId", payload.userId());
        //body.put("role", payload.role().name());
        body.put("accessToken", tokens.accessToken());
        body.put("refreshToken", tokens.refreshToken());

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
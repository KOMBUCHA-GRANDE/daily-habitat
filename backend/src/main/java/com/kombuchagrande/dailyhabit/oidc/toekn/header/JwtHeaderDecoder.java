package com.kombuchagrande.dailyhabit.oidc.toekn.header;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@RequiredArgsConstructor
public final class JwtHeaderDecoder {

    private final ObjectMapper om;

    /**
     * JWT의 <header>.<payload>.<signature> 중 header를 Base64Url 디코드하여 DTO로 변환
     */
    public JwtHeaderDto decode(String jwt) {
        try {
            // 정확히 3파트로 제한 (불필요한 split 방지 위해 limit=3)
            String[] parts = jwt.split("\\.", 3);
            if (parts.length != 3 || parts[0].isEmpty() || parts[1].isEmpty() || parts[2].isEmpty()) {
                throw new IllegalArgumentException("Invalid JWT format");
            }
            String headerJson = new String(
                    Base64.getUrlDecoder().decode(parts[0]),
                    java.nio.charset.StandardCharsets.UTF_8
            );
            return om.readValue(headerJson, JwtHeaderDto.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("INVALID_JWT_HEADER: invalid jwt header", e);
        }
    }
}

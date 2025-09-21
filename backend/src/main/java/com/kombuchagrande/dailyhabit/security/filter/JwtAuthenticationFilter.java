package com.kombuchagrande.dailyhabit.security.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kombuchagrande.dailyhabit.security.jwt.JwtService;
import com.kombuchagrande.dailyhabit.security.jwt.JwtTokenProvider;
import com.kombuchagrande.dailyhabit.security.jwt.dto.JwtAccessPayloadDto;
import com.kombuchagrande.dailyhabit.security.userdetails.CustomUserDetails;
import com.kombuchagrande.dailyhabit.security.jwt.dto.JwtPayloadDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;


    private static final AntPathMatcher MATCHER = new AntPathMatcher();

    // 필요 시 추가(화이트리스트)
    private final Set<String> whitelist = Set.of(
            "/auth/login",
            "/auth/logout",
            "/auth/logout/all",
            "/auth/token/refresh",
            "/actuator/health"
    );


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        for (String p : whitelist) {
            if (MATCHER.match(p, path)) return true;
        }
        return false;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String auth = request.getHeader("Authorization");

        if (auth == null || !auth.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = auth.substring(7);

        try {
            Claims claims = jwtTokenProvider.parseClaimsStrict(token);           // 검증+파싱(1회)
            JwtAccessPayloadDto payload = JwtAccessPayloadDto.fromClaims(claims); // DTO 매핑

            // 컷오프 검사
            if (jwtService.isAfterUserCutoff(payload.userId(), payload.iat())) {
                write401(response, "USER_CUTOFF", "User has logged out from all devices");
                return;
            }
            if (jwtService.isAfterDeviceCutoff(payload.tid(), payload.iat())) {
                write401(response, "DEVICE_CUTOFF", "This device session has been revoked");
                return;
            }

            // SecurityContext 주입
            JwtPayloadDto principalPayload = new JwtPayloadDto(payload.userId(), payload.role());
            CustomUserDetails principal = new CustomUserDetails(principalPayload);
            UsernamePasswordAuthenticationToken authentication
                    = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            write401(response, "ACCESS_TOKEN_EXPIRED", "Access token expired");
        } catch (JwtException e) {
            write401(response, "ACCESS_TOKEN_INVALID", "Invalid or expired access token");
        }
    }

    private void write401(HttpServletResponse response, String code, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        Map<String, Object> err = new LinkedHashMap<>();
        err.put("code", code);
        err.put("message", message);
        objectMapper.writeValue(response.getOutputStream(), err);
    }
}
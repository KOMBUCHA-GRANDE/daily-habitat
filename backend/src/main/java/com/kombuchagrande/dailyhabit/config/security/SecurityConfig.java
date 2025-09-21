package com.kombuchagrande.dailyhabit.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kombuchagrande.dailyhabit.security.filter.OidcLoginAuthenticationFilter;
import com.kombuchagrande.dailyhabit.security.handler.JsonAccessDeniedHandler;
import com.kombuchagrande.dailyhabit.security.handler.JsonAuthenticationEntryPoint;
import com.kombuchagrande.dailyhabit.security.handler.OidcLoginFailureHandler;
import com.kombuchagrande.dailyhabit.security.handler.OidcLoginSuccessHandler;
import com.kombuchagrande.dailyhabit.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(List<AuthenticationProvider> providers) {
        return new ProviderManager(providers);
    }

    @Bean
    public OidcLoginAuthenticationFilter oidcLoginAuthenticationFilter(
            AuthenticationManager authenticationManager,
            ObjectMapper objectMapper,
            OidcLoginSuccessHandler successHandler,
            OidcLoginFailureHandler failureHandler
    ) {
        OidcLoginAuthenticationFilter filter = new OidcLoginAuthenticationFilter(authenticationManager, objectMapper);
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failureHandler);
        return filter;
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            OidcLoginAuthenticationFilter oidcLoginAuthenticationFilter,
            JsonAuthenticationEntryPoint entryPoint,
            JsonAccessDeniedHandler accessDeniedHandler,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/token/refresh").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/logout").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/logout/all").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(configurer -> configurer
                        .authenticationEntryPoint(entryPoint) //401
                        .accessDeniedHandler(accessDeniedHandler) //403
                )
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(oidcLoginAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtAuthenticationFilter, OidcLoginAuthenticationFilter.class)
                .build();
    }
}
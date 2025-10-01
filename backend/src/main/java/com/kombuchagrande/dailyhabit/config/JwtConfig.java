package com.kombuchagrande.dailyhabit.config;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.clock-skew-seconds:0}")
    private long skew;

    @Bean
    public SecretKey jwtSigningKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secret);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException ignore) {
            return Keys.hmacShaKeyFor(secret.getBytes());
        }
    }

    @Bean
    public JwtParser strictJwtParser(SecretKey key) {
        JwtParserBuilder b = Jwts.parser().verifyWith(key);
        if (skew > 0) b.clockSkewSeconds(skew);
        return b.build();
    }
}
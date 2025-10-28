package com.kombuchagrande.dailyhabit.oidc.keys.fetch;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class RestJwksFetcher {

    private final RestClient jwksRestClient;

    public String fetchJson(String jwksUri) {
        return jwksRestClient.get()
                .uri(jwksUri)
                .header("Accept", "application/json")
                .retrieve()
                .body(String.class);
    }
}
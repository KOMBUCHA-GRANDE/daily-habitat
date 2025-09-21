package com.kombuchagrande.dailyhabit.oidc.keys.fetch;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class RestJwksFetcher implements JwksFetcher {

    private final RestClient jwksRestClient;

    @Override
    public String fetchJson(String jwksUri) {
        return jwksRestClient.get()
                .uri(jwksUri)
                .header("Accept", "application/json")
                .retrieve()
                .body(String.class);
    }
}
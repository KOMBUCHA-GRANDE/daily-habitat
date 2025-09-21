package com.kombuchagrande.dailyhabit.oidc.keys.fetch;

public interface JwksFetcher {
    String fetchJson(String jwksUri);
}
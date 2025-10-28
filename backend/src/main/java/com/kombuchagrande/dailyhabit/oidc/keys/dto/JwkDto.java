package com.kombuchagrande.dailyhabit.oidc.keys.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record JwkDto(
        String kty,
        String kid,
        String use,
        String alg,
        String n,
        String e
) {}
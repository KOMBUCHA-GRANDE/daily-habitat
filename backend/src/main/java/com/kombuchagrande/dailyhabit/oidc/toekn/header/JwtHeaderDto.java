package com.kombuchagrande.dailyhabit.oidc.toekn.header;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record JwtHeaderDto(
        String alg,
        String kid
) {}
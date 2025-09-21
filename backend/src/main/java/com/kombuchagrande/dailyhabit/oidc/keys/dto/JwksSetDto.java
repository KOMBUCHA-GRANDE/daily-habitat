package com.kombuchagrande.dailyhabit.oidc.keys.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record JwksSetDto(
        List<JwkDto> keys
) {}

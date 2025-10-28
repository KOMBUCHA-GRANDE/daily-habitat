package com.kombuchagrande.dailyhabit.oidc.dto;


import com.kombuchagrande.dailyhabit.entity.enums.ProviderType;

public record VerifiedOidc(
        ProviderType providerType,
        String sub // user unique id
) {}

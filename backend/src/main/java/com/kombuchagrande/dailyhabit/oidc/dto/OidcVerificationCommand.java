package com.kombuchagrande.dailyhabit.oidc.dto;

import com.kombuchagrande.dailyhabit.entity.enums.ProviderType;

public record OidcVerificationCommand(
        ProviderType provider,
        String idToken
) {}
package com.kombuchagrande.dailyhabit.security.jwt.dto;

import org.springframework.security.core.userdetails.UserDetails;

public record LoadOrCreateResult(
        UserDetails userDetails,
        boolean isNewUser
) {}
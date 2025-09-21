package com.kombuchagrande.dailyhabit.security.jwt.dto;

import com.kombuchagrande.dailyhabit.entity.User;
import com.kombuchagrande.dailyhabit.entity.enums.Role;

public record JwtPayloadDto(
        Long userId,
        Role role
) {
    public static JwtPayloadDto fromUser(User user) {
        return new JwtPayloadDto(
                user.getId(),
                user.getRole()
        );
    }
}
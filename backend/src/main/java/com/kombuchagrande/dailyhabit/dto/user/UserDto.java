package com.kombuchagrande.dailyhabit.dto.user;

public record UserDto(
        Long id,
        String nickname,
        boolean notificationEnabled
) {

}

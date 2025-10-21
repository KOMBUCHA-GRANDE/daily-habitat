package com.kombuchagrande.dailyhabit.dto.user;

import com.kombuchagrande.dailyhabit.common.validation.NotBlankIfPresent;

public record UserUpdateRequest(
        @NotBlankIfPresent(message = "닉네임은 공백일 수 없습니다.")
        String nickname,
        Boolean notificationEnabled
) {
}

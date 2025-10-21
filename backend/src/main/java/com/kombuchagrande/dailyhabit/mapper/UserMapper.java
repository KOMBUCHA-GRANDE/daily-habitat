package com.kombuchagrande.dailyhabit.mapper;


import com.kombuchagrande.dailyhabit.dto.user.UserDto;
import com.kombuchagrande.dailyhabit.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getNickname(),
                user.isNotificationEnabled()
        );
    }
}

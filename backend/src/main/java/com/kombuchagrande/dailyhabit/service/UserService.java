package com.kombuchagrande.dailyhabit.service;

import com.kombuchagrande.dailyhabit.dto.user.UserDto;
import com.kombuchagrande.dailyhabit.entity.User;
import com.kombuchagrande.dailyhabit.mapper.UserMapper;
import com.kombuchagrande.dailyhabit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto get(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException()); //Todo 커스텀 예외처리

        return userMapper.toDto(user);
    }

}


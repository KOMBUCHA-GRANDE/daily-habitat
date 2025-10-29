package com.kombuchagrande.dailyhabit.service;

import com.kombuchagrande.dailyhabit.dto.user.UserDto;
import com.kombuchagrande.dailyhabit.dto.user.UserUpdateRequest;
import com.kombuchagrande.dailyhabit.entity.User;
import com.kombuchagrande.dailyhabit.mapper.UserMapper;
import com.kombuchagrande.dailyhabit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

    @Transactional
    public UserDto update(Long userId, UserUpdateRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException());//Todo 커스텀 예외처리

        if(request.nickname() != null) {
            user.updateNickname(request.nickname());
        }
        if(request.notificationEnabled() != null) {
            user.updateNotificationEnabled(request.notificationEnabled());
        }

        return userMapper.toDto(user);
    }

    @Transactional
    public void softDelete(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException());//Todo 커스텀 예외처리

        //Todo 추후 복구 로직 생각하기 (로그인 시, 논리 삭제된 계정인지 구분하는 과정 필요)
        //Todo 특정 시간 이후 DB 에서 스케줄링으로 지우기 (연관관계 생각해서 제거)
        user.softDelete();
    }
}


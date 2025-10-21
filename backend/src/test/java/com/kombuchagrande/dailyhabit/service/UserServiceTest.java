package com.kombuchagrande.dailyhabit.service;

import com.kombuchagrande.dailyhabit.dto.user.UserDto;
import com.kombuchagrande.dailyhabit.entity.User;
import com.kombuchagrande.dailyhabit.entity.enums.ProviderType;
import com.kombuchagrande.dailyhabit.entity.enums.Role;
import com.kombuchagrande.dailyhabit.mapper.UserMapper;
import com.kombuchagrande.dailyhabit.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @InjectMocks
    UserService userService;

    private Long userId;
    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userId = 1L;
        user = User.builder()
                .nickname("nick1")
                .providerType(ProviderType.KAKAO)
                .role(Role.USER)
                .providerId("providerId")
                .notificationEnabled(true)
                .build();

        ReflectionTestUtils.setField(user, "id", userId);

        userDto = new UserDto(1L, "nick1", true);
    }

    @DisplayName("유저를 조회할 수 있다.")
    @Test
    void getUser() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        //when
        UserDto result = userService.get(userId);

        //then
        assertThat(result).isSameAs(userDto);
        verify(userRepository).findById(userId);
    }

    @DisplayName("유저가 없다면 예외를 반환한다.")
    @Test
    void get_notFound_throws() {
        //given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //when then
        assertThatThrownBy(() -> userService.get(userId))
                .isInstanceOf(IllegalArgumentException.class);
    }



}
package com.kombuchagrande.dailyhabit.service;

import com.kombuchagrande.dailyhabit.dto.user.UserDto;
import com.kombuchagrande.dailyhabit.dto.user.UserUpdateRequest;
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
import static org.mockito.BDDMockito.given;

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
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(userMapper.toDto(user)).willReturn(userDto);

        //when
        UserDto result = userService.get(userId);

        //then
        assertThat(result).isEqualTo(userDto);
        verify(userRepository).findById(userId);
    }

    @DisplayName("유저가 없다면 예외를 반환한다.")
    @Test
    void get_notFound_throws() {
        //given
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        //when then
        assertThatThrownBy(() -> userService.get(userId))
                .isInstanceOf(IllegalArgumentException.class); //Todo 커스텀 예외처리
    }

    @DisplayName("유저 정보를 수정할 수 있다.")
    @Test
    void updateUser() {
        //given
        UserUpdateRequest request = new UserUpdateRequest("새 닉네임", true);
        UserDto newUserDto = new UserDto(1L, "새 닉네임", true);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(userMapper.toDto(user)).willReturn(newUserDto);

        //when
        UserDto result = userService.update(userId, request);

        //then
        assertThat(result).isEqualTo(newUserDto);
        assertThat(user.getNickname()).isEqualTo("새 닉네임");
    }

    @DisplayName("유저 업데이트 시, 유저가 존재하지 않으면 예외가 발생한다.")
    @Test
    void update_notFound_throws() {
        //given
        UserUpdateRequest request = new UserUpdateRequest("새 닉테임", true);
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        //when then
        assertThatThrownBy(() -> userService.update(userId, request))
                .isInstanceOf(IllegalArgumentException.class); //Todo 커스텀 예외처리
    }

    @DisplayName("사용자의 id로 논리 삭제가 가능하다.")
    @Test
    void softSoftDeleteUser() {
        //given
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        //when
        userService.softDelete(userId);

        //then
        assertThat(user.isDeleted()).isTrue();
    }

    @DisplayName("삭제 시, 유저가 존재하지 않으면 예외가 발생한다.")
    @Test
    void softSoftDelete_notFound_throws() {
        //given
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        //when then
        assertThatThrownBy(() -> userService.softDelete(userId))
                .isInstanceOf(IllegalArgumentException.class); //Todo 커스텀 예외처리
    }


}
package com.kombuchagrande.dailyhabit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kombuchagrande.dailyhabit.dto.user.UserDto;
import com.kombuchagrande.dailyhabit.dto.user.UserUpdateRequest;
import com.kombuchagrande.dailyhabit.entity.enums.Role;
import com.kombuchagrande.dailyhabit.security.jwt.JwtService;
import com.kombuchagrande.dailyhabit.security.jwt.JwtTokenProvider;
import com.kombuchagrande.dailyhabit.security.jwt.dto.JwtPayloadDto;
import com.kombuchagrande.dailyhabit.security.userdetails.CustomUserDetails;
import com.kombuchagrande.dailyhabit.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.given;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    UserService userService;

    private long userId;
    private CustomUserDetails principal;
    private UserDto userDto;

    @BeforeEach
    void setup() {
        userId = 1L;
        principal = new CustomUserDetails(
                new JwtPayloadDto(userId, Role.USER));

        userDto = new UserDto(1L, "nickname1", true);
    }

    @DisplayName("인증 객체를 가지고 있는 사용자를 조회할 수 있다.")
    @Test
    void me_authenticated_returnDto() throws Exception {
        //given
        given(userService.get(userId)).willReturn(userDto);

        //when then
        mockMvc.perform(get("/api/users/me")
                        .with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));

    }

    @DisplayName("인증 객체를 가지고 있는 사용자 정보 수정할 수 있다.")
    @Test
    void update_authenticated_returnDto() throws Exception{
        //given
        UserUpdateRequest request = new UserUpdateRequest("새 닉네임", true);
        UserDto newUserDto = new UserDto(1L, "새 닉네임", true);
        given(userService.update(userId, request)).willReturn(newUserDto);

        //when then
        mockMvc.perform(patch("/api/users")
                        .with(csrf())
                        .with(user(principal))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(newUserDto)));

        verify(userService).update(eq(userId), any(UserUpdateRequest.class));
    }

    @DisplayName("인증 객체를 가지고 있는 사용자 정보 수정할 수 있다.")
    @Test
    void update_blankNickname_returnsBadRequest() throws Exception{
        //given
        UserUpdateRequest request = new UserUpdateRequest("", true);
        UserDto newUserDto = new UserDto(1L, "새 닉네임", true);
        given(userService.update(userId, request)).willReturn(newUserDto);

        //when then
        mockMvc.perform(patch("/api/users")
                        .with(csrf())
                        .with(user(principal))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(MethodArgumentNotValidException.class));
    }

    @DisplayName("인증 객체를 가지고 있는 사용자를 논리 삭제할 수 있다.")
    @Test
    void softDelete_authenticated() throws Exception {

        //when then
        mockMvc.perform(delete("/api/users")
                        .with(csrf())
                        .with(user(principal)))
                .andExpect(status().isNoContent());
        verify(userService).softDelete(userId);
    }


}
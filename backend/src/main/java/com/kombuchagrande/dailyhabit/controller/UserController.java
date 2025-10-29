package com.kombuchagrande.dailyhabit.controller;


import com.kombuchagrande.dailyhabit.dto.user.UserDto;
import com.kombuchagrande.dailyhabit.dto.user.UserUpdateRequest;
import com.kombuchagrande.dailyhabit.security.userdetails.CustomUserDetails;
import com.kombuchagrande.dailyhabit.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(@AuthenticationPrincipal CustomUserDetails user) {
        UserDto userDto = userService.get(user.getUserId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userDto);
    }

    @PatchMapping
    public ResponseEntity<UserDto> update(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody @Valid UserUpdateRequest request
    ) {
        UserDto userDto = userService.update(user.getUserId(), request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userDto);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.softDelete(userDetails.getUserId());

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}

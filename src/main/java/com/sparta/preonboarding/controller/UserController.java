package com.sparta.preonboarding.controller;

import com.sparta.preonboarding.dto.UserRequestDto;
import com.sparta.preonboarding.dto.UserResponseDto;
import com.sparta.preonboarding.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class UserController {
  private final UserService userService;

  @PostMapping("/signup")
  @Operation(summary = "회원가입", description = "회원가입을 수행합니다.")
  public ResponseEntity<UserResponseDto> signup(@RequestBody UserRequestDto userRequestDto) {
    UserResponseDto userResponseDto = userService.signup(userRequestDto);
    return ResponseEntity.ok(userResponseDto);
  }
}

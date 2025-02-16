package com.sparta.preonboarding.controller;

import com.sparta.preonboarding.dto.LoginRequestDto;
import com.sparta.preonboarding.dto.LoginResponseDto;
import com.sparta.preonboarding.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {
  private final AuthService authService;

  @PostMapping("/sign")
  @Operation(summary = "로그인", description = "로그인을 수행합니다.")
  public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
    LoginResponseDto token = authService.login(loginRequestDto);
    return ResponseEntity.ok(token);
  }

  @PostMapping("/re-issue")
  @Operation(summary = "토큰 재발급", description = "리프레시 토큰을 이용하여 액세스 토큰을 재발급합니다.")
  public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
    return authService.reissue(request, response);

  }
}

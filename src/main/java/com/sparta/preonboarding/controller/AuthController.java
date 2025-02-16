package com.sparta.preonboarding.controller;

import com.sparta.preonboarding.dto.LoginRequestDto;
import com.sparta.preonboarding.dto.LoginResponseDto;
import com.sparta.preonboarding.service.AuthService;
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
  public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
    LoginResponseDto token = authService.login(loginRequestDto);
    return ResponseEntity.ok(token);
  }

  @PostMapping("/re-issue")
  public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
    return authService.reissue(request, response);

  }
}

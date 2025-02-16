package com.sparta.preonboarding.controller;

import com.sparta.preonboarding.dto.UserRequestDto;
import com.sparta.preonboarding.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class UserController {
  private final UserService userService;

  @PostMapping("/signup")
  public String signup(@RequestBody UserRequestDto userRequestDto) {
    userService.signup(userRequestDto);

    return "signup";
  }
}

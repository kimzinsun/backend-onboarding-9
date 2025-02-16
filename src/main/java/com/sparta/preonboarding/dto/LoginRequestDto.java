package com.sparta.preonboarding.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
  @Schema(description = "사용자 이름", example = "Jin Ho", required = true)
  private String username;
  @Schema(description = "비밀번호", example = "12341234", required = true)
  private String password;

}

package com.sparta.preonboarding.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
  private String username;
  private String nickname;
  private List<AuthorityDto> authorities;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class AuthorityDto {
    private String authorityName;
  }
}

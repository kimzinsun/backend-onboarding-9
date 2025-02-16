package com.sparta.preonboarding.service;

import com.sparta.preonboarding.domain.model.User;
import com.sparta.preonboarding.domain.repository.UserRepository;
import com.sparta.preonboarding.dto.UserRequestDto;
import com.sparta.preonboarding.dto.UserResponseDto;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Transactional
  public UserResponseDto signup(UserRequestDto userRequestDto) {
    String username = userRequestDto.getUsername();
    String nickname = userRequestDto.getNickname();
    String password = userRequestDto.getPassword();

    validateDuplicateUser(username, nickname);

    User user = User.builder()
        .username(username)
        .nickname(nickname)
        .password(bCryptPasswordEncoder.encode(password))
        .role("USER")
        .build();

    userRepository.save(user);

    List<UserResponseDto.AuthorityDto> authorities = List.of(
        UserResponseDto.AuthorityDto.builder()
            .authorityName("ROLE_USER")
            .build()
    );

    return UserResponseDto.builder()
        .username(user.getUsername())
        .nickname(user.getNickname())
        .authorities(authorities)
        .build();
  }

  private void validateDuplicateUser(String username, String nickname) {
    if(userRepository.existsByUsername(username)) {
      throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
    }

    if(userRepository.existsByNickname(nickname)) {
      throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
    }
  }

}

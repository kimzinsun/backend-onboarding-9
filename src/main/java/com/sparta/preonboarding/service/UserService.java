package com.sparta.preonboarding.service;

import com.sparta.preonboarding.domain.model.User;
import com.sparta.preonboarding.domain.repository.UserRepository;
import com.sparta.preonboarding.dto.UserRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  public void signup(UserRequestDto userRequestDto) {
    String username = userRequestDto.getUsername();
    String nickname = userRequestDto.getNickname();
    String password = userRequestDto.getPassword();

    if(userRepository.existsByUsername(username)) {
      throw new IllegalArgumentException("이미 존재하는 username입니다.");
    }

    if(userRepository.existsByNickname(nickname)) {
      throw new IllegalArgumentException("이미 존재하는 nickname입니다.");
    }

    User user = User.builder()
        .username(username)
        .nickname(nickname)
        .password(bCryptPasswordEncoder.encode(password))
        .role("USER")
        .build();

    userRepository.save(user);
  }

}

package com.sparta.preonboarding.service;

import com.sparta.preonboarding.domain.model.User;
import com.sparta.preonboarding.domain.repository.UserRepository;
import com.sparta.preonboarding.dto.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;


  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);

    if(user != null) {
      return new CustomUserDetails(user);
    }

    return null;
  }

}

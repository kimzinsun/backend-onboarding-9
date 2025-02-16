package com.sparta.preonboarding.domain.repository;

import com.sparta.preonboarding.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Boolean existsByUsername(String username);
  Boolean existsByNickname(String nickname);
  User findByUsername(String username);

}

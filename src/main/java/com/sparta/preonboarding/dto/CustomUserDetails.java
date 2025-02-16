package com.sparta.preonboarding.dto;

import com.sparta.preonboarding.domain.model.User;
import java.util.ArrayList;
import java.util.Collection;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

  private final User user;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {

    Collection<GrantedAuthority> authorities = new ArrayList<>();

    authorities.add(new GrantedAuthority() {
      @Override
      public String getAuthority() {
        return "ROLE_" + user.getRole();
      }
    });
    return authorities;
  }

    @Override
    public String getPassword () {
      return user.getPassword();
    }

    @Override
    public String getUsername () {
      return user.getUsername();
    }

  }

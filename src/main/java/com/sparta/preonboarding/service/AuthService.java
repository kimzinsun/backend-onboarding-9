package com.sparta.preonboarding.service;

import com.sparta.preonboarding.dto.LoginRequestDto;
import com.sparta.preonboarding.dto.LoginResponseDto;
import com.sparta.preonboarding.infra.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

  private final JwtUtil jwtUtil;
  private final AuthenticationManager authenticationManager;


  public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
    String refresh = null;
    Cookie[] cookies = request.getCookies();

    for (Cookie cookie : cookies) {
      if (cookie.getName().equals("refresh")) {
        refresh = cookie.getValue();
      }
    }

    if (refresh == null) {
      return ResponseEntity.badRequest().build();
    }

    try {
      jwtUtil.isExpired(refresh);
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }

    String category = jwtUtil.getCategory(refresh);

    if (!category.equals("refresh")) {
      return ResponseEntity.badRequest().build();
    }

    String username = jwtUtil.getUsername(refresh);
    String role = jwtUtil.getRole(refresh);

    String accessToken = jwtUtil.createJwt("access", username, role,
        JwtUtil.ACCESS_TOKEN_EXPIRE_TIME);

    response.setHeader(JwtUtil.ACCESS_TOKEN_HEADER, JwtUtil.BEARER_PREFIX + accessToken);

    return ResponseEntity.ok().build();

  }

  public LoginResponseDto login(LoginRequestDto loginRequestDto) {
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              loginRequestDto.getUsername(), loginRequestDto.getPassword()
          )
      );

      String username = authentication.getName();
      String role = authentication.getAuthorities().stream()
          .findFirst()
          .map(GrantedAuthority::getAuthority)
          .orElse("ROLE_USER");

      String accessToken = jwtUtil.createJwt("access", username, role, JwtUtil.ACCESS_TOKEN_EXPIRE_TIME);
      String refreshToken = jwtUtil.createJwt("refresh", username, role, JwtUtil.REFRESH_TOKEN_EXPIRE_TIME);

      return new LoginResponseDto(accessToken);
    } catch (AuthenticationException e) {
      throw new RuntimeException("Invalid credentials");
    }
  }


}

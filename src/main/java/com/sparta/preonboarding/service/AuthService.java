package com.sparta.preonboarding.service;

import com.sparta.preonboarding.infra.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

  private final JwtUtil jwtUtil;

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

}

package com.sparta.preonboarding.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AllArgsConstructor
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

    String username = obtainUsername(request);
    String password = obtainPassword(request);

    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password, null);

    return authenticationManager.authenticate(authenticationToken);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authentication) {
      String username = authentication.getName();
      String role = extractRole(authentication);

      String accessToken = jwtUtil.createJwt("access", username, role, JwtUtil.ACCESS_TOKEN_EXPIRE_TIME);
      String refreshToken = jwtUtil.createJwt("refresh", username, role, JwtUtil.REFRESH_TOKEN_EXPIRE_TIME);

      response.setHeader(JwtUtil.ACCESS_TOKEN_HEADER, JwtUtil.BEARER_PREFIX + accessToken);
      response.addCookie(createCookie(refreshToken));
      response.setStatus(HttpServletResponse.SC_OK);

  }

  private String extractRole(Authentication authentication) {
    return authentication.getAuthorities().stream()
        .findFirst()
        .map(GrantedAuthority::getAuthority)
        .orElse("ROLE_USER");
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
    log.info("##### unsuccessfulAuthentication");

  }

  private Cookie createCookie(String value) {
    Cookie cookie = new Cookie("refresh", value);
    cookie.setHttpOnly(true);
    cookie.setMaxAge((int) JwtUtil.REFRESH_TOKEN_EXPIRE_TIME / 1000);
    cookie.setSecure(true);
    cookie.setPath("/");
    return cookie;
  }

}

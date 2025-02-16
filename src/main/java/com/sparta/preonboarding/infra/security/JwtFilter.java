package com.sparta.preonboarding.infra.security;

import com.sparta.preonboarding.domain.model.User;
import com.sparta.preonboarding.dto.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
  private final JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String authorization = request.getHeader(JwtUtil.ACCESS_TOKEN_HEADER);

    if (authorization == null || !authorization.startsWith(JwtUtil.BEARER_PREFIX)) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = authorization.substring(JwtUtil.BEARER_PREFIX.length());

    if (jwtUtil.isExpired(token)) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    String username = jwtUtil.getUsername(token);
    String role = jwtUtil.getRole(token);

    User user = new User();
    user.setUsername(username);
    user.setRole(role);

    CustomUserDetails customUserDetails = new CustomUserDetails(user);
    Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null,
        customUserDetails.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(authToken);

    filterChain.doFilter(request, response);

  }

}

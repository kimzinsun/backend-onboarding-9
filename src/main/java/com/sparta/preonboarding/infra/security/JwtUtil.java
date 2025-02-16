package com.sparta.preonboarding.infra.security;

import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtUtil {

  public static final String ACCESS_TOKEN_HEADER = "Authorization";
  public static final String REFRESH_TOKEN_HEADER = "Refresh-Token";
  public static final String BEARER_PREFIX = "Bearer ";

  private static final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L;
  private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;

  private SecretKey secretKey;

  public JwtUtil(@Value("${jwt.secret.key}") String secret) {
    this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
  }


  public String getUsername(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .get("username", String.class);
  }

  public String getRole(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .get("role", String.class);
  }

  public Boolean isExpired(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getExpiration()
        .before(new Date());
  }

  public String createJwt(String username, String role, Long expireTime) {
    return Jwts.builder()
        .claim("username", username)
        .claim("role", role)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + expireTime))
        .signWith(secretKey)
        .compact();
  }

}

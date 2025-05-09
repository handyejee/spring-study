package com.spring.study.springsecurity.jwt;

import com.spring.study.springsecurity.user.domain.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class JwtTokenProvider {

  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String AUTHORIZATION_KEY = "auth";

  private final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L; //1시간
  private final long REFRESH_TOKEN_TIME = 7 * 24 * 60 * 60 * 1000L; // 7일

  @Value("${jwt.secret.key}")
  private String secretKey;
  private SecretKey key;

  @PostConstruct
  public void init() {
    byte[] bytes = Base64.getDecoder().decode(secretKey);
    key = Keys.hmacShaKeyFor(bytes);
  }

  public String createAccessToken(Long id, UserRole role) {
    Date date = new Date();

    return Jwts.builder()
            .subject(String.valueOf(id))
            .claim(AUTHORIZATION_KEY, role.getAuthority())
            .claim("tokenType", "ACCESS")
            .expiration(new Date(date.getTime() + ACCESS_TOKEN_TIME))
            .issuedAt(date)
            .signWith(key)
            .compact();
  }

  public String createRefreshToken(Long id, UserRole role) {
    Date date = new Date();

    return Jwts.builder()
            .subject(id.toString())
            .claim(AUTHORIZATION_KEY, role)
            .claim("tokenType", "REFRESH")
            .expiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
            .issuedAt(date)
            .signWith(key)
            .compact();
  }

  // 토큰에서 사용자 정보 가져오기
  public Claims getUserInfoFromToken(String token) {
    return Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  // header 에서 JWT 가져오기
  public String getJwtFromHeader(HttpServletRequest request) {
    String authHeader = request.getHeader(AUTHORIZATION_HEADER);
    if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
      return authHeader.substring(7);
    }
    return null;
  }

  // 토큰 검증
  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(key)
          .build().
          parseSignedClaims(token);
      return true;

    } catch (SecurityException | MalformedJwtException e) {
      log.error("Invalid JWT signature");
      throw new AuthenticationException("유효하지 않은 토큰입니다.") {};
    } catch (ExpiredJwtException e) {
      log.error("Expired JWT token");
      throw new AuthenticationException("만료된 토큰입니다.") {};
    } catch (UnsupportedJwtException e) {
      log.error("Unsupported JWT token");
      throw new AuthenticationException("지원되지 않는 토큰입니다.") {};
    } catch (IllegalArgumentException e) {
      log.error("JWT claims is empty");
      throw new AuthenticationException("잘못된 JWT 토큰 입니다.") {};
    }
  }
}

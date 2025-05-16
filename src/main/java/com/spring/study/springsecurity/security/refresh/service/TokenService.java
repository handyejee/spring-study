package com.spring.study.springsecurity.security.refresh.service;

import com.spring.study.springsecurity.jwt.JwtTokenProvider;
import com.spring.study.springsecurity.security.refresh.domain.RefreshToken;
import com.spring.study.springsecurity.user.domain.UserRole;
import io.jsonwebtoken.Claims;

import lombok.AllArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TokenService {

  private final RefreshTokenService refreshTokenService;
  private final JwtTokenProvider jwtTokenProvider;

  public String createNewAccessToken(String refreshToken) {
    if (!jwtTokenProvider.validateToken(refreshToken)) {
      throw new IllegalArgumentException("유효하지 않은 Refresh Token입니다.");
    }

    try {
      Claims claims = jwtTokenProvider.getUserInfoFromToken(refreshToken);

      String tokenType = claims.get("tokenType", String.class);
      if (!"REFRESH".equals(tokenType)) {
        throw new IllegalArgumentException("올바른 Refresh Token이 아닙니다.");
      }

      RefreshToken savedToken = refreshTokenService.findByRefreshToken(refreshToken);
      Long userId = savedToken.getUserId();

      // 토큰의 사용자 ID와 DB에서 찾은 사용자 ID 일치 여부 확인
      String subjectId = claims.getSubject();
      if (!userId.toString().equals(subjectId)) {
        throw new IllegalArgumentException("토큰의 사용자 정보가 일치하지 않습니다.");
      }

      String roleStr = claims.get(JwtTokenProvider.AUTHORIZATION_KEY, String.class);
      UserRole role = UserRole.valueOf(roleStr);

      // access token 발급
      return jwtTokenProvider.createAccessToken(userId, role);

    } catch (AuthenticationException e) {
      // validateToken에서 발생하는 AuthenticationException을 처리
      throw new IllegalArgumentException("Refresh Token 검증 실패: " + e.getMessage(), e);
    } catch (Exception e) {
      throw new IllegalArgumentException("토큰 처리 중 오류 발생: " + e.getMessage(), e);
    }
  }
}

package com.spring.study.springsecurity.security.refresh.service;

import com.spring.study.springsecurity.security.refresh.domain.RefreshToken;
import com.spring.study.springsecurity.security.refresh.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;

  public RefreshToken findByRefreshToken(String refreshToken) {
    return refreshTokenRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new IllegalArgumentException("Unexpected Token"));
  }

  @Transactional
  public RefreshToken saveRefreshToken(Long userId, String refreshToken) {
    // 기존 토큰이 있는지 확인
    return refreshTokenRepository.findByUserId(userId)
        .map(existingToken -> {
          // 기존 토큰이 있으면 업데이트(Optional이 값을 가지고 있을때 실행)
          log.info("기존 리프레시 토큰 업데이트: 사용자 ID={}", userId);
          existingToken.updateToken(refreshToken, LocalDateTime.now().plusDays(7));
          return refreshTokenRepository.save(existingToken);
        })
        .orElseGet(() -> {
          // 기존 토큰이 없으면 새로 생성
          log.info("새 리프레시 토큰 생성: 사용자 ID={}", userId);
          RefreshToken tokenEntity = RefreshToken.builder()
              .userId(userId)
              .refreshToken(refreshToken)
              .expiryDate(LocalDateTime.now().plusDays(7))
              .build();
          return refreshTokenRepository.save(tokenEntity);
        });
  }
}

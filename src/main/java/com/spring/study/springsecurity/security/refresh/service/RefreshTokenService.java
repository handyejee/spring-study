package com.spring.study.springsecurity.security.refresh.service;

import com.spring.study.springsecurity.security.refresh.domain.RefreshToken;
import com.spring.study.springsecurity.security.refresh.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    refreshTokenRepository.findByUserId(userId)
        .ifPresent(refreshTokenRepository::delete);

    // 만료 시간 계산 (7일)
    LocalDateTime expiryDate = LocalDateTime.now().plusDays(7);

    // 새 토큰 생성 및 저장
    RefreshToken tokenEntity = RefreshToken.builder()
        .userId(userId)
        .refreshToken(refreshToken) // 필드명이 refreshToken으로 가정
        .expiryDate(expiryDate)
        .build();

    return refreshTokenRepository.save(tokenEntity);
  }
}

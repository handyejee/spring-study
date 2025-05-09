package com.spring.study.springsecurity.security.refresh.repository;

import com.spring.study.springsecurity.security.refresh.domain.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByRefreshToken(String refreshToken);
  Optional<RefreshToken> findByUserId(Long userId);
}

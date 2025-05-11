package com.spring.study.springsecurity.security.refresh.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "refresh_tokens")
@NoArgsConstructor
public class RefreshToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = false, unique = true, length = 500)
  private String refreshToken;

  @Column(nullable = false)
  private LocalDateTime expiryDate;

  public void updateToken(String newToken, LocalDateTime newExpiryDate) {
    this.refreshToken = newToken;
    this.expiryDate = newExpiryDate;
  }

  @Builder
  public RefreshToken(Long id, Long userId, String refreshToken, LocalDateTime expiryDate) {
    this.id = id;
    this.userId = userId;
    this.refreshToken = refreshToken;
    this.expiryDate = expiryDate;
  }
}

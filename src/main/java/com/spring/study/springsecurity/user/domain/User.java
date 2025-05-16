package com.spring.study.springsecurity.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private UserRole role;

  @Column
  private String provider; // 인증서버

  @Column
  private String providerId; // OAuth2 제공자가 제공한 고유 ID


  @Builder
  public User(String username, String email, String password, UserRole role, String provider, String providerId) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.role = role;
    this.provider = provider;
    this.providerId = providerId;
  }

  public User update(String username) {
    if (username != null) {
      this.username = username;
    }
    return this;
  }
}

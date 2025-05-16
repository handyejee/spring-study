package com.spring.study.springsecurity.user.dto;

import com.spring.study.springsecurity.user.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto {

  private Long userId;
  private String email;
  private String username;

  private String accessToken;
  private String refreshToken;
  private UserRole role;
}

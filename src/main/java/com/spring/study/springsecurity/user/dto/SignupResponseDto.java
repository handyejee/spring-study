package com.spring.study.springsecurity.user.dto;

import com.spring.study.springsecurity.user.domain.User;
import com.spring.study.springsecurity.user.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SignupResponseDto {

  private String username;
  private String email;
  private UserRole role;

  public static SignupResponseDto from(User user) {
    return SignupResponseDto.builder()
        .username(user.getUsername())
        .email(user.getEmail())
        .role(user.getRole())
        .build();
  }
}

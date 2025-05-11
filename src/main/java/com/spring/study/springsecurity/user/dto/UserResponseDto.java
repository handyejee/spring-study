package com.spring.study.springsecurity.user.dto;

import com.spring.study.springsecurity.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

  private Long id;
  private String username;
  private String email;
  private String role;

  public static UserResponseDto from(User user) {
    return new UserResponseDto(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getRole().name()
    );
  }
}

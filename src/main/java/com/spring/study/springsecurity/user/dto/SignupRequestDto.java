package com.spring.study.springsecurity.user.dto;

import com.spring.study.springsecurity.user.domain.User;
import com.spring.study.springsecurity.user.domain.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {
  @NotBlank
  private String username;

  @NotBlank
  @Email
  private String email;

  @NotBlank
  private String password;

  @NotNull
  private UserRole role;

  public User toEntity(BCryptPasswordEncoder encoder) {
    return User.builder()
        .email(this.email)
        .password(encoder.encode(password))
        .username(this.username)
        .role(this.role)
        .build();
  }
}

package com.spring.study.springsecurity.oauth;

import java.security.SecureRandom;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordUtils {

  private final PasswordEncoder passwordEncoder;

  public String generatePassword() {
    byte[] randomBytes = new byte[32];
    new SecureRandom().nextBytes(randomBytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
  }

  public String generateAndEncodePassword() {
    String rawPassword = generatePassword();
    return passwordEncoder.encode(rawPassword);
  }
}

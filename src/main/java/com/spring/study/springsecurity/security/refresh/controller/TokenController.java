package com.spring.study.springsecurity.security.refresh.controller;

import com.spring.study.springsecurity.security.refresh.dto.CreateAccessTokenRequest;
import com.spring.study.springsecurity.security.refresh.dto.CreateAccessTokenResponse;
import com.spring.study.springsecurity.security.refresh.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class TokenController {

  private final TokenService tokenService;

  @PostMapping("/refresh")
  public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(
      @RequestBody CreateAccessTokenRequest request) {
    log.info("Received refresh token request: {}", request);

    try {
      // refreshToken이 null인지 확인
      if (request.getRefreshToken() == null) {
        log.error("Refresh token is null");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
      }

      String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());

      // 새 토큰이 생성되었는지 확인
      log.info("New access token generated: {}", newAccessToken != null);

      return ResponseEntity.status(HttpStatus.CREATED)
          .body(new CreateAccessTokenResponse(newAccessToken));
    } catch (Exception e) {
      log.error("Error creating new access token", e);
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
  }
}
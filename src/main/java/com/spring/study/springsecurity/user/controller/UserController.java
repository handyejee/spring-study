package com.spring.study.springsecurity.user.controller;

import com.spring.study.springsecurity.user.dto.LoginRequestDto;
import com.spring.study.springsecurity.user.dto.LoginResponseDto;
import com.spring.study.springsecurity.user.dto.SignupRequestDto;
import com.spring.study.springsecurity.user.dto.SignupResponseDto;
import com.spring.study.springsecurity.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/signup")
  public ResponseEntity<SignupResponseDto> signup(@RequestBody @Valid SignupRequestDto request) {
    SignupResponseDto response = userService.signupUser(request);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto request) {
    return ResponseEntity.ok(userService.login(request));
  }
}

package com.spring.study.springsecurity.user.service;

import com.spring.study.springsecurity.user.domain.User;
import com.spring.study.springsecurity.user.dto.LoginRequestDto;
import com.spring.study.springsecurity.user.dto.LoginResponseDto;
import com.spring.study.springsecurity.user.dto.SignupRequestDto;
import com.spring.study.springsecurity.user.dto.SignupResponseDto;
import com.spring.study.springsecurity.user.dto.UserResponseDto;
import com.spring.study.springsecurity.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

  private final UserRepository userRepository;
  private final AuthenticationManager authenticationManager;
  private final BCryptPasswordEncoder passwordEncoder;

  @Transactional
  public SignupResponseDto signupUser(SignupRequestDto request) {

    if (userRepository.existsByEmail(request.getEmail())) {
      throw new IllegalArgumentException("중복된 Email 입니다.");
    }

    return SignupResponseDto.fromEntity(
        userRepository.save(request.toEntity(passwordEncoder)));
  }

  @Transactional(readOnly = true)
  public LoginResponseDto login(LoginRequestDto request) {
    // 인증 시도
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
    );

    // SecurityContext에 인증 정보 저장
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // 로그인 성공한 유저 정보 조회
    User user = userRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

    return new LoginResponseDto(user.getId(), user.getEmail(), user.getUsername());
  }

  @Transactional(readOnly = true)
  public UserResponseDto getUserById(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + userId));

    return UserResponseDto.fromEntity(user);

  }

  @Transactional(readOnly = true)
  public List<UserResponseDto> getAllUsers() {
    return userRepository.findAll().stream()
        .map(UserResponseDto::fromEntity)
        .collect(Collectors.toList());
  }

  @Transactional
  public void deleteUser(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + userId));
    userRepository.delete(user);
  }
}

package com.spring.study.springsecurity.user.service;

import com.spring.study.springsecurity.exception.CustomException;
import com.spring.study.springsecurity.exception.ErrorCode;
import com.spring.study.springsecurity.user.domain.User;
import com.spring.study.springsecurity.user.dto.SignupRequestDto;
import com.spring.study.springsecurity.user.dto.SignupResponseDto;
import com.spring.study.springsecurity.user.dto.UserResponseDto;
import com.spring.study.springsecurity.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public SignupResponseDto signupUser(SignupRequestDto request) {

    if (userRepository.existsByEmail(request.getEmail())) {
      throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
    }

    return SignupResponseDto.from(
        userRepository.save(request.toEntity(passwordEncoder)));
  }

  /*
   * JWT 인증 방식 도입으로 인해 더 이상 사용되지 않음.
   * JwtAuthenticationFilter에서 인증 처리를 담당함.
   */
//  @Transactional(readOnly = true)
//  public LoginResponseDto login(LoginRequestDto request) {
//    // 인증 시도
//    Authentication authentication = authenticationManager.authenticate(
//        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
//    );
//
//    // SecurityContext에 인증 정보 저장
//    SecurityContextHolder.getContext().setAuthentication(authentication);
//
//    // 로그인 성공한 유저 정보 조회
//    User user = userRepository.findByUsername(request.getUsername())
//        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
//
//    return new LoginResponseDto(user.getId(), user.getEmail(), user.getUsername());
//  }

  @Transactional(readOnly = true)
  public UserResponseDto getUserById(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    return UserResponseDto.from(user);

  }

  @Transactional(readOnly = true)
  public List<UserResponseDto> getAllUsers() {
    return userRepository.findAll().stream()
        .map(UserResponseDto::from)
        .collect(Collectors.toList());
  }

  @Transactional
  public void deleteUser(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    userRepository.delete(user);
  }
}

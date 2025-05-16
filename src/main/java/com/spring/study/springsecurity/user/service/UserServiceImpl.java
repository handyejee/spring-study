package com.spring.study.springsecurity.user.service;

import com.spring.study.springsecurity.exception.CustomException;
import com.spring.study.springsecurity.exception.ErrorCode;
import com.spring.study.springsecurity.user.domain.User;
import com.spring.study.springsecurity.user.dto.UserResponseDto;
import com.spring.study.springsecurity.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

  private final UserRepository userRepository;

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

package com.spring.study.springsecurity.user.service;

import com.spring.study.springsecurity.user.dto.UserResponseDto;
import java.util.List;

public interface UserService {

  UserResponseDto getUserById(Long userId);
  List<UserResponseDto> getAllUsers();
  void deleteUser(Long userId);
}

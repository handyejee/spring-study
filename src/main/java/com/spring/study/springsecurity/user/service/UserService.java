package com.spring.study.springsecurity.user.service;

import com.spring.study.springsecurity.user.dto.LoginRequestDto;
import com.spring.study.springsecurity.user.dto.LoginResponseDto;
import com.spring.study.springsecurity.user.dto.SignupRequestDto;
import com.spring.study.springsecurity.user.dto.SignupResponseDto;
import com.spring.study.springsecurity.user.dto.UserResponseDto;
import java.util.List;

public interface UserService {

  SignupResponseDto signupUser(SignupRequestDto request);
//  LoginResponseDto login(LoginRequestDto request);
  UserResponseDto getUserById(Long userId);
  List<UserResponseDto> getAllUsers();
  void deleteUser(Long userId);
}

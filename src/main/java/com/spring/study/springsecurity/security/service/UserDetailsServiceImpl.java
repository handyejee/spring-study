package com.spring.study.springsecurity.security.service;

import com.spring.study.springsecurity.security.principle.PrincipalDetails;
import com.spring.study.springsecurity.user.domain.User;
import com.spring.study.springsecurity.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Deprecated
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Not Found " + username));

    return new PrincipalDetails(user);
  }

  public UserDetails loadUserById(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다. ID: " + id));

    return new PrincipalDetails(user);
  }
}

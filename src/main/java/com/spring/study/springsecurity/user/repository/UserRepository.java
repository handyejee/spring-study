package com.spring.study.springsecurity.user.repository;

import com.spring.study.springsecurity.user.domain.User;
import com.spring.study.springsecurity.user.dto.UserResponseDto;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);
  Boolean existsByEmail(String email);
}

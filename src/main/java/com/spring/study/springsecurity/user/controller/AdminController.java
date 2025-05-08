package com.spring.study.springsecurity.user.controller;

import com.spring.study.springsecurity.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

  private final UserService userService;

  //  @Secured("ROLE_ADMIN")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable("id") Long userId) {
    userService.deleteUser(userId);
    return ResponseEntity.noContent().build();
  }
}

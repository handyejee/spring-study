package com.spring.study.springsecurity.exception;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class ErrorResponse {

  private LocalDateTime timestamp;
  private int status;
  private String message;
}

package com.spring.study.springsecurity.exception;

import java.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
    ErrorResponse response = new ErrorResponse(
        LocalDateTime.now(),
        ex.getErrorCode().getStatus().value(),
        ex.getErrorCode().getMessage()
    );
    return ResponseEntity.status(ex.getErrorCode().getStatus()).body(response);
  }
}

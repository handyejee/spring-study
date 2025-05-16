package com.spring.study.springsecurity.oauth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException {

    String errorMessage = exception.getMessage();
    if (errorMessage == null || errorMessage.isEmpty()) {
      errorMessage = "로그인 처리 중 오류가 발생했습니다.";
    }

    errorMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8.toString());

    String targetUrl = UriComponentsBuilder.fromUriString("/login-failed")
        .queryParam("error", errorMessage)
        .build().toUriString();

    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }
}

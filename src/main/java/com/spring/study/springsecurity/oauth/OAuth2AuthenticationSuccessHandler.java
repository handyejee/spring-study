package com.spring.study.springsecurity.oauth;

import com.spring.study.springsecurity.jwt.JwtTokenProvider;
import com.spring.study.springsecurity.security.principle.PrincipalDetails;
import com.spring.study.springsecurity.security.refresh.service.RefreshTokenService;
import com.spring.study.springsecurity.user.domain.User;
import com.spring.study.springsecurity.user.domain.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final JwtTokenProvider jwtTokenProvider;
  private final RefreshTokenService refreshTokenService;

  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {
    log.debug(
        "========== OAuth2AuthenticationSuccessHandler.onAuthenticationSuccess() 시작 ==========");

    PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
    User user = principal.getUser();

    Long userId = user.getId();
    UserRole userRole = user.getRole();

    log.info("사용자 정보: id={}, role={}", userId, userRole);

    // JWT 토큰 생성
    String accessToken = jwtTokenProvider.createAccessToken(userId, userRole);
    String refreshToken = jwtTokenProvider.createRefreshToken(userId, userRole);

    // 리프레시 토큰 저장
    refreshTokenService.saveRefreshToken(userId, refreshToken);

    // 리다이렉트
    String targetUrl = UriComponentsBuilder.fromUriString("/login-success")
        .queryParam("accessToken", accessToken)
        .queryParam("refreshToken", refreshToken)
        .build().toUriString();

    log.info("로그인 성공: userId={}, 리다이렉트={}", userId, targetUrl);
    getRedirectStrategy().sendRedirect(request, response, targetUrl);

    log.info(
        "========== OAuth2AuthenticationSuccessHandler.onAuthenticationSuccess() 종료 ==========");
  }
}

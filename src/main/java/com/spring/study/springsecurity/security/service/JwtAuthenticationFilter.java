package com.spring.study.springsecurity.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.study.springsecurity.jwt.JwtTokenProvider;
import com.spring.study.springsecurity.security.principle.PrincipalDetails;
import com.spring.study.springsecurity.security.refresh.service.RefreshTokenService;
import com.spring.study.springsecurity.user.domain.User;
import com.spring.study.springsecurity.user.domain.UserRole;
import com.spring.study.springsecurity.user.dto.LoginRequestDto;
import com.spring.study.springsecurity.user.dto.LoginResponseDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final JwtTokenProvider jwtTokenProvider;
  private final RefreshTokenService refreshTokenService;

  {
    setFilterProcessesUrl("/api/auth/login");
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    try {
      LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(),
          LoginRequestDto.class);

      return getAuthenticationManager().authenticate(
          new UsernamePasswordAuthenticationToken(
              requestDto.getUsername(),
              requestDto.getPassword(),
              null
          )
      );
    } catch (IOException e) {
      log.error(e.getMessage());
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult)
      throws IOException {
    PrincipalDetails userDetails = (PrincipalDetails) authResult.getPrincipal();
    User user = userDetails.getUser();
    UserRole role = user.getRole();
    Long userId = user.getId();
    String email = user.getEmail();
    String username = user.getUsername();

    String accessToken = jwtTokenProvider.createAccessToken(userId, role);
    String refreshToken = jwtTokenProvider.createRefreshToken(userId, role);

    refreshTokenService.saveRefreshToken(userId, refreshToken);

    LoginResponseDto loginResponseDto = LoginResponseDto.builder()
        .userId(userId)
        .email(email)
        .username(username)
        .role(role)
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();

    response.setContentType("application/json");
    new ObjectMapper().writeValue(response.getWriter(), loginResponseDto);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException failed) {

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 인증 실패 시 401 돌려주도록
  }
}

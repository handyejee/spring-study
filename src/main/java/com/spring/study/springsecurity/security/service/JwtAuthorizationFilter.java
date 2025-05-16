package com.spring.study.springsecurity.security.service;

import com.spring.study.springsecurity.jwt.JwtTokenProvider;
import com.spring.study.springsecurity.security.principle.PrincipalDetails;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

  private final UserDetailsServiceImpl userDetailsService;
  private final JwtTokenProvider jwtTokenProvider;

  @Override
  protected void doFilterInternal(
      HttpServletRequest req,
      HttpServletResponse res,
      FilterChain filterChain
  ) throws ServletException, IOException {

    log.debug("=== JwtAuthorizationFilter starting ===");
    String token = jwtTokenProvider.getJwtFromHeader(req); // 토큰 추출

    if (StringUtils.hasText(token)) {
      try {
        jwtTokenProvider.validateToken(token); // 서명 및 만료 시간 검사
        Claims claims = jwtTokenProvider.getUserInfoFromToken(token); // 토큰에서 사용자 정보 추출

        Long userId = Long.parseLong(claims.getSubject());
        PrincipalDetails userDetails = (PrincipalDetails) userDetailsService.loadUserById(userId);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.debug("사용자 인증 성공: ID={}", userId);
      } catch (Exception e) {
        log.error("JWT 인증 처리 중 오류 발생: {}", e.getMessage());
        SecurityContextHolder.clearContext(); // 인증 실패 시 SecurityContext 초기화
      }
    }

    filterChain.doFilter(req, res);
    log.debug("=== JwtAuthorizationFilter completed ===");
  }
}

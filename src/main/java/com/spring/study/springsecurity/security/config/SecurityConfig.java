package com.spring.study.springsecurity.security.config;

import com.spring.study.springsecurity.jwt.JwtTokenProvider;
import com.spring.study.springsecurity.oauth.CustomOAuth2UserService;
import com.spring.study.springsecurity.oauth.OAuth2AuthenticationFailureHandler;
import com.spring.study.springsecurity.oauth.OAuth2AuthenticationSuccessHandler;
import com.spring.study.springsecurity.security.refresh.service.RefreshTokenService;
import com.spring.study.springsecurity.security.service.JwtAuthenticationFilter;
import com.spring.study.springsecurity.security.service.JwtAuthorizationFilter;
import com.spring.study.springsecurity.security.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
//@EnableMethodSecurity() // 메소드 기반 권한 부여
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final UserDetailsServiceImpl userDetailsService;
  private final JwtTokenProvider jwtTokenProvider;
  private final AuthenticationConfiguration authenticationConfiguration;
  private final RefreshTokenService refreshTokenService;

  private final CustomOAuth2UserService customOAuth2UserService;
  private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
  private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
      throws Exception {
    return configuration.getAuthenticationManager();
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) throws Exception {
    JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtTokenProvider, refreshTokenService);
    filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));

    return filter;
  }

  @Bean
  public JwtAuthorizationFilter jwtAuthorizationFilter() {
    return new JwtAuthorizationFilter(userDetailsService, jwtTokenProvider);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        )
        .httpBasic(AbstractHttpConfigurer::disable) // httpBasic 인증 비활성화

        .oauth2Login(oauth2 -> {
          log.info("OAuth2 로그인 설정");
          oauth2
              .userInfoEndpoint(userInfo -> {
                log.info("userInfoEndpoint 설정");
                userInfo.userService(customOAuth2UserService);
                log.info("CustomOAuth2UserService 등록됨");
              })
              .successHandler(oAuth2AuthenticationSuccessHandler)
              .failureHandler(oAuth2AuthenticationFailureHandler);
          log.info("OAuth2 설정 완료");
        })

        .addFilter(jwtAuthenticationFilter(jwtTokenProvider)) // 로그인 처리 필터
        .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class) // 토큰 검증 필터

        .authorizeHttpRequests(auth -> auth
        .requestMatchers(
            "/api/auth/signup",
            "/api/auth/login",
            "/api/token/refresh",
            "/oauth2/**",
            "/login/**",
            "/",            // 메인 페이지
            "/error"        // 에러 페이지
        ).permitAll() // 인증 필요없는 url 설정

        // 사용자 삭제
        .requestMatchers("/api/admin/**").hasAuthority("DELETE")

        // 전체 사용자 목록 조회 - ADMIN 역할
        .requestMatchers(HttpMethod.GET, "/api/users").hasAnyRole("ADMIN", "MANAGER")  // 전체 사용자 조회 (관리자만)

        // 사용자 단건조회(본인 or MANAGER)
        .requestMatchers(HttpMethod.GET, "/api/users/{id}").access(
            new WebExpressionAuthorizationManager(
                "hasRole('MANAGER') or #userId == authentication.principal.id")
        )
         .anyRequest().authenticated()
        );

    return http.build();
  }
}

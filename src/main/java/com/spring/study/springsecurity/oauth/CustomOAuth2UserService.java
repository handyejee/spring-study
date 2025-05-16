package com.spring.study.springsecurity.oauth;

import com.spring.study.springsecurity.security.principle.PrincipalDetails;
import com.spring.study.springsecurity.user.domain.User;
import com.spring.study.springsecurity.user.domain.UserRole;
import com.spring.study.springsecurity.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final UserRepository userRepository;
  private final PasswordUtils passwordUtils;

  // OAuth 인증 후 사용자 정보 처리
  @Override
  @Transactional
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    log.info("===== CustomOAuth2UserService.loadUser 실행 =====");

    OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
    OAuth2User oAuth2User = delegate.loadUser(userRequest);

    log.info("OAuth2 원본 응답: {}", oAuth2User.getAttributes());

    // OAuth2 서비스 ID
    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
        .getUserInfoEndpoint().getUserNameAttributeName();

    Map<String, Object> attributes = oAuth2User.getAttributes();
    String providerId = (String) attributes.get(userNameAttributeName);
    String email = (String) attributes.get("email");
    String name = (String) attributes.get("name");

    // 사용자 저장 또는 업데이트
    User user = saveOrUpdate(email, name, registrationId, providerId);
    log.info("사용자 저장/업데이트 완료: id={}, email={}", user.getId(), user.getEmail());

    log.info("===== CustomOAuth2UserService.loadUser 완료 =====");
    return new PrincipalDetails(user, attributes);
  }

  private User saveOrUpdate(String email, String name, String provider, String providerId) {
    User user = userRepository.findByEmail(email)
        .map(existingUser -> {
          // 기존 사용자 업데이트 로직
          return existingUser.update(name);
        })
        .orElseGet(() -> {

          String encodedPassword = passwordUtils.generateAndEncodePassword();

          // 새 사용자 생성 로직
          return User.builder()
              .username(name)
              .email(email)
              .password(encodedPassword)
              .role(UserRole.USER) // 기본 권한
              .provider(provider)
              .providerId(providerId)
              .build();
        });

    User savedUser = userRepository.save(user);
    log.debug("사용자 저장 완료: id={}, email={}", savedUser.getId(), savedUser.getEmail());
    return savedUser;
  }
}

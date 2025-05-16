package com.spring.study.springsecurity.security.principle;

import com.spring.study.springsecurity.user.domain.User;
import com.spring.study.springsecurity.user.domain.UserPermission;
import com.spring.study.springsecurity.user.domain.UserRole;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Slf4j
@Getter
public class PrincipalDetails implements UserDetails, OAuth2User {

  private final User user;
  private final Map<String, Object> attributes;

  public PrincipalDetails(User user) {
    this.user = user;
    this.attributes = Map.of(); // 빈 맵
  }

  public PrincipalDetails(User user, Map<String, Object> attributes) {
    this.user = user;
    this.attributes = attributes;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> authorities = new ArrayList<>();

    // 역할 기반 권한
    UserRole role = user.getRole();
    String authority = role.getAuthority();
    authorities.add(new SimpleGrantedAuthority(authority));

    // 역할에 매핑된 권한
    for (UserPermission permission : role.getPermissions()) {
      log.info("추가 권한: {}", permission.getPermission());
      authorities.add(new SimpleGrantedAuthority(permission.getPermission()));
    }

    return authorities;
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  public Long getId() {
    return user.getId();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  @Override
  public String getName() {
    return user.getUsername(); // OAuth2User의 name으로 username 반환
  }
}

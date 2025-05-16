package com.spring.study.springsecurity.security.service;

import com.spring.study.springsecurity.user.domain.User;
import com.spring.study.springsecurity.user.domain.UserPermission;
import com.spring.study.springsecurity.user.domain.UserRole;
import java.util.ArrayList;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Deprecated
@Slf4j
public class UserDetailsImpl implements UserDetails {

  private final User user;

  public UserDetailsImpl(User user) {
    this.user = user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> authorities = new ArrayList<>();

    //역할 기반 권한
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

  public User getUser() {
    return user;
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

}

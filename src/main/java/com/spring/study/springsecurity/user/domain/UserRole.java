package com.spring.study.springsecurity.user.domain;

import java.util.HashSet;
import java.util.Set;

public enum UserRole {

  USER(Authority.USER),
  ADMIN(Authority.ADMIN),
  MANAGER(Authority.MANAGER);

  private final String authority;

  UserRole(String authority) {
    this.authority = authority;
  }

  public String getAuthority() {
    return this.authority;
  }

  public static class Authority {

    public static final String USER = "ROLE_USER";
    public static final String ADMIN = "ROLE_ADMIN";
    public static final String MANAGER = "ROLE_MANAGER";
  }

  public Set<UserPermission> getPermissions() {
    Set<UserPermission> permissions = new HashSet<>();

    // 모든 사용자는 READ 권한을 가짐
    permissions.add(UserPermission.READ);

    // ADMIN 역할은 DELETE 권한 가짐
    if (this == ADMIN) {
      permissions.add(UserPermission.DELETE);
    }

    // MANAGER 역할은 WRITE 권한 가짐
    if (this == MANAGER) {
      permissions.add(UserPermission.WRITE);
    }

    return permissions;
  }
}

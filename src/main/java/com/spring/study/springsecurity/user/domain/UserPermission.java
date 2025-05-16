package com.spring.study.springsecurity.user.domain;

public enum UserPermission {
  READ("READ"),
  WRITE("WRITE"),
  DELETE("DELETE");


  private final String permission;

  UserPermission(String permission) {
    this.permission = permission;
  }

  public String getPermission() {
    return this.permission;
  }
}

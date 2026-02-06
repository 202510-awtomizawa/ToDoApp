package com.example.todo.form;

import jakarta.validation.constraints.NotBlank;

public class AdminUserEditForm {
  @NotBlank
  private String role;

  private boolean enabled;

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}

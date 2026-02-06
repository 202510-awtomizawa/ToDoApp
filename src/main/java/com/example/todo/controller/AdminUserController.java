package com.example.todo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.todo.entity.AppUser;
import com.example.todo.form.AdminUserEditForm;
import com.example.todo.repository.UserRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {
  private final UserRepository userRepository;

  public AdminUserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GetMapping
  public String list(Model model) {
    List<AppUser> users = userRepository.findAll(Sort.by("username").ascending());
    model.addAttribute("users", users);
    return "admin/users";
  }

  @GetMapping("/{id}/edit")
  public String edit(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
    Optional<AppUser> userOpt = userRepository.findById(id);
    if (userOpt.isEmpty()) {
      redirectAttributes.addFlashAttribute("message", "ユーザーが見つかりません。");
      return "redirect:/admin/users";
    }
    AppUser user = userOpt.get();
    AdminUserEditForm form = new AdminUserEditForm();
    form.setRole(user.getRole() == null || user.getRole().isBlank() ? "USER" : user.getRole());
    form.setEnabled(user.isEnabled());
    model.addAttribute("user", user);
    model.addAttribute("form", form);
    return "admin/user_edit";
  }

  @PostMapping("/{id}")
  public String update(@PathVariable("id") Integer id,
      @Valid AdminUserEditForm form,
      BindingResult result,
      Model model,
      RedirectAttributes redirectAttributes) {
    Optional<AppUser> userOpt = userRepository.findById(id);
    if (userOpt.isEmpty()) {
      redirectAttributes.addFlashAttribute("message", "ユーザーが見つかりません。");
      return "redirect:/admin/users";
    }
    String role = normalizeRole(form.getRole());
    if (role == null) {
      result.rejectValue("role", "invalid", "ロールはADMINまたはUSERを指定してください。");
    }
    if (result.hasErrors()) {
      model.addAttribute("user", userOpt.get());
      return "admin/user_edit";
    }
    AppUser user = userOpt.get();
    user.setRole(role);
    user.setEnabled(form.isEnabled());
    userRepository.save(user);
    redirectAttributes.addFlashAttribute("message", "ユーザーを更新しました。");
    return "redirect:/admin/users";
  }

  private String normalizeRole(String role) {
    if (role == null) {
      return null;
    }
    String normalized = role.trim().toUpperCase();
    if ("ADMIN".equals(normalized) || "USER".equals(normalized)) {
      return normalized;
    }
    return null;
  }
}

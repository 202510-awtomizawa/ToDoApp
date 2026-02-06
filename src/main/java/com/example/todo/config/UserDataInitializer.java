package com.example.todo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.todo.entity.AppUser;
import com.example.todo.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class UserDataInitializer implements CommandLineRunner {
  private static final Logger log = LoggerFactory.getLogger(UserDataInitializer.class);
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserDataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  @Override
  public void run(String... args) {
    createOrFixUser("admin", "password", "admin@example.com", "ADMIN");
    createOrFixUser("user1", "password", "user1@example.com", "USER");
  }

  private void createOrFixUser(String username, String rawPassword, String email, String role) {
    AppUser user = userRepository.findByUsername(username).orElseGet(AppUser::new);
    boolean isNew = user.getId() == null;

    if (isNew) {
      user.setUsername(username);
    }
    if (user.getEmail() == null || user.getEmail().isBlank()) {
      user.setEmail(email);
    }
    if (user.getRole() == null || user.getRole().isBlank()) {
      user.setRole(role);
    }
    user.setEnabled(true);

    if (isNew || !passwordEncoder.matches(rawPassword, user.getPassword())) {
      user.setPassword(passwordEncoder.encode(rawPassword));
      userRepository.saveAndFlush(user);
      log.info(isNew ? "User created: {}" : "User password fixed: {}", username);
      return;
    }

    userRepository.saveAndFlush(user);
    log.info("User already exists: {}", username);
  }
}

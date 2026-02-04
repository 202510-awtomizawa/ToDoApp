package com.example.todo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.todo.entity.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Integer> {
  Optional<AppUser> findByUsername(String username);
}

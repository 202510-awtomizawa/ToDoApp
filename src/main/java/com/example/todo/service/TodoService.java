package com.example.todo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;

import com.example.todo.entity.AppUser;
import com.example.todo.entity.Priority;
import com.example.todo.entity.Todo;
import com.example.todo.repository.TodoRepository;

@Service
public class TodoService {
  private final TodoRepository todoRepository;
  private final CategoryService categoryService;

  public TodoService(TodoRepository todoRepository, CategoryService categoryService) {
    this.todoRepository = todoRepository;
    this.categoryService = categoryService;
  }

  public List<Todo> findAll(String keyword, Long categoryId, Priority priority, AppUser user, Sort sort) {
    String q = keyword == null ? null : keyword.trim();
    return todoRepository.search(q, categoryId, priority, user, sort);
  }

  public Optional<Todo> findByIdForUser(Long id, AppUser user) {
    return todoRepository.findByIdAndUser(id, user);
  }

  public List<Todo> findAllByUser(AppUser user) {
    return todoRepository.findAllByUser(user, Sort.by("createdAt").descending());
  }

  public List<Todo> findAllByIdsForUser(List<Long> ids, AppUser user) {
    return todoRepository.findAllByIdInAndUser(ids, user);
  }

  public List<Todo> findOverdue(LocalDate date) {
    return todoRepository.findByDeadlineBefore(date);
  }

  @Transactional
  public Todo saveForUser(Todo todo, AppUser user) {
    todo.setUser(user);
    if (todo.getId() != null) {
      todoRepository.findByIdAndUser(todo.getId(), user).ifPresentOrElse(existing -> {
        todo.setCreatedAt(existing.getCreatedAt());
      }, () -> {
        throw new AccessDeniedException("Not allowed");
      });
    }
    if (todo.getPriority() == null) {
      todo.setPriority(Priority.MEDIUM);
    }
    normalizeCategory(todo);
    return todoRepository.save(todo);
  }

  @Transactional
  public void toggleCompleted(Long id, AppUser user) {
    todoRepository.findByIdAndUser(id, user).ifPresent(todo -> {
      todo.setCompleted(!todo.isCompleted());
      todoRepository.save(todo);
    });
  }

  @Transactional
  public void delete(Long id, AppUser user) {
    todoRepository.findByIdAndUser(id, user).ifPresent(todoRepository::delete);
  }

  @Transactional
  public void deleteAllByIds(List<Long> ids, AppUser user) {
    List<Todo> owned = todoRepository.findAllByIdInAndUser(ids, user);
    todoRepository.deleteAllInBatch(owned);
  }

  private void normalizeCategory(Todo todo) {
    if (todo.getCategory() == null || todo.getCategory().getId() == null) {
      todo.setCategory(null);
      return;
    }
    Long categoryId = todo.getCategory().getId();
    todo.setCategory(categoryService.findById(categoryId).orElse(null));
  }
}


package com.example.todo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.todo.entity.Todo;
import com.example.todo.repository.TodoRepository;

@Service
public class TodoService {
  private final TodoRepository todoRepository;

  public TodoService(TodoRepository todoRepository) {
    this.todoRepository = todoRepository;
  }

  public List<Todo> findAll(String keyword, Sort sort) {
    if (keyword == null || keyword.trim().isEmpty()) {
      return todoRepository.findAll(sort);
    }
    String q = keyword.trim();
    return todoRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(q, q, sort);
  }

  public Optional<Todo> findById(Long id) {
    return todoRepository.findById(id);
  }

  public List<Todo> findAllByIds(List<Long> ids) {
    return todoRepository.findAllById(ids);
  }

  @Transactional
  public Todo save(Todo todo) {
    if (todo.getId() != null) {
      todoRepository.findById(todo.getId()).ifPresent(existing -> {
        todo.setCreatedAt(existing.getCreatedAt());
      });
    }
    return todoRepository.save(todo);
  }

  @Transactional
  public void toggleCompleted(Long id) {
    todoRepository.findById(id).ifPresent(todo -> {
      todo.setCompleted(!todo.isCompleted());
      todoRepository.save(todo);
    });
  }

  @Transactional
  public void delete(Long id) {
    todoRepository.deleteById(id);
  }

  @Transactional
  public void deleteAllByIds(List<Long> ids) {
    todoRepository.deleteAllByIdInBatch(ids);
  }
}


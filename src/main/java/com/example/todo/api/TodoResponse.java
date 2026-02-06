package com.example.todo.api;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.todo.entity.Category;
import com.example.todo.entity.Priority;
import com.example.todo.entity.Todo;

public class TodoResponse {
  private Long id;
  private String author;
  private String title;
  private String detail;
  private boolean completed;
  private Priority priority;
  private LocalDate deadline;
  private Long categoryId;
  private String categoryName;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static TodoResponse fromEntity(Todo todo) {
    TodoResponse res = new TodoResponse();
    res.setId(todo.getId());
    res.setAuthor(todo.getAuthor());
    res.setTitle(todo.getTitle());
    res.setDetail(todo.getDetail());
    res.setCompleted(todo.isCompleted());
    res.setPriority(todo.getPriority());
    res.setDeadline(todo.getDeadline());
    Category category = todo.getCategory();
    if (category != null) {
      res.setCategoryId(category.getId());
      res.setCategoryName(category.getName());
    }
    res.setCreatedAt(todo.getCreatedAt());
    res.setUpdatedAt(todo.getUpdatedAt());
    return res;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }

  public boolean isCompleted() {
    return completed;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  public Priority getPriority() {
    return priority;
  }

  public void setPriority(Priority priority) {
    this.priority = priority;
  }

  public LocalDate getDeadline() {
    return deadline;
  }

  public void setDeadline(LocalDate deadline) {
    this.deadline = deadline;
  }

  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}

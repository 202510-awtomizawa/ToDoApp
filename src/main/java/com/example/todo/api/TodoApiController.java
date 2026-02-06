package com.example.todo.api;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo.entity.AppUser;
import com.example.todo.entity.Category;
import com.example.todo.entity.Todo;
import com.example.todo.repository.UserRepository;
import com.example.todo.service.TodoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin
public class TodoApiController {
  private final TodoService todoService;
  private final UserRepository userRepository;

  public TodoApiController(TodoService todoService, UserRepository userRepository) {
    this.todoService = todoService;
    this.userRepository = userRepository;
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<TodoResponse>>> list(Principal principal) {
    Optional<AppUser> user = resolveUser(principal);
    if (user.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error("User not found", null));
    }
    List<TodoResponse> todos = todoService.findAllByUserOrAll(user.get()).stream()
        .map(TodoResponse::fromEntity)
        .toList();
    return ResponseEntity.ok(ApiResponse.ok("OK", todos));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<TodoResponse>> get(@PathVariable Long id, Principal principal) {
    Optional<AppUser> user = resolveUser(principal);
    if (user.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error("User not found", null));
    }
    return todoService.findByIdForUserOrAll(id, user.get())
        .map(todo -> ResponseEntity.ok(ApiResponse.ok("OK", TodoResponse.fromEntity(todo))))
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.error("Todo not found", null)));
  }

  @PostMapping
  public ResponseEntity<ApiResponse<TodoResponse>> create(@Valid @RequestBody TodoRequest request,
      Principal principal) {
    Optional<AppUser> user = resolveUser(principal);
    if (user.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error("User not found", null));
    }
    Todo todo = new Todo();
    applyRequest(todo, request);
    Todo saved = todoService.saveForUser(todo, user.get());
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.ok("Created", TodoResponse.fromEntity(saved)));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<TodoResponse>> update(@PathVariable Long id,
      @Valid @RequestBody TodoRequest request, Principal principal) {
    Optional<AppUser> user = resolveUser(principal);
    if (user.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error("User not found", null));
    }
    Optional<Todo> existing = todoService.findByIdForUserOrAll(id, user.get());
    if (existing.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error("Todo not found", null));
    }
    Todo todo = existing.get();
    applyRequest(todo, request);
    todo.setId(id);
    Todo saved = todoService.saveForUser(todo, user.get());
    return ResponseEntity.ok(ApiResponse.ok("Updated", TodoResponse.fromEntity(saved)));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Long id, Principal principal) {
    Optional<AppUser> user = resolveUser(principal);
    if (user.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error("User not found", null));
    }
    Optional<Todo> existing = todoService.findByIdForUserOrAll(id, user.get());
    if (existing.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponse.error("Todo not found", null));
    }
    todoService.delete(id, user.get());
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Object>> handleValidation(MethodArgumentNotValidException ex) {
    String message = "Validation error";
    FieldError fieldError = ex.getBindingResult().getFieldError();
    if (fieldError != null) {
      message = fieldError.getField() + ": " + fieldError.getDefaultMessage();
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ApiResponse.error(message, null));
  }

  private Optional<AppUser> resolveUser(Principal principal) {
    if (principal == null) {
      return Optional.empty();
    }
    return userRepository.findByUsername(principal.getName());
  }

  private void applyRequest(Todo todo, TodoRequest request) {
    todo.setAuthor(request.getAuthor());
    todo.setTitle(request.getTitle());
    todo.setDetail(request.getDetail());
    todo.setCompleted(Boolean.TRUE.equals(request.getCompleted()));
    todo.setPriority(request.getPriority());
    todo.setDeadline(request.getDeadline());
    if (request.getCategoryId() != null) {
      Category category = new Category();
      category.setId(request.getCategoryId());
      todo.setCategory(category);
    } else {
      todo.setCategory(null);
    }
  }
}

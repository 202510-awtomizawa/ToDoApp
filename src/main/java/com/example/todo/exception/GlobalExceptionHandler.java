package com.example.todo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = "com.example.todo.controller")
public class GlobalExceptionHandler {
  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(TodoNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String handleTodoNotFound(TodoNotFoundException ex, Model model) {
    logger.error("Todo not found", ex);
    model.addAttribute("message", ex.getMessage());
    return "error/404";
  }

  @ExceptionHandler(BusinessException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handleBusiness(BusinessException ex, Model model) {
    logger.error("Business error", ex);
    model.addAttribute("message", ex.getMessage());
    return "error/500";
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public String handleGeneral(Exception ex, Model model) {
    logger.error("Unexpected error", ex);
    model.addAttribute("message", "Unexpected error occurred.");
    return "error/500";
  }
}

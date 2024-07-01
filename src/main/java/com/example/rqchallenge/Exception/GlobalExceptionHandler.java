package com.example.rqchallenge.Exception;

import com.example.rqchallenge.model.CustomValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<CustomValidationError> handleCustomException(CustomException ex) {

    String errors = ex.getMessage();

    CustomValidationError customValidationError = new CustomValidationError(LocalDateTime.now().toString(),
        HttpStatus.BAD_REQUEST.value(), errors, getRequestPath());

    return ResponseEntity.badRequest().body(customValidationError);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<CustomValidationError> handleException(Exception ex) {

    String errors = ex.getMessage();

    CustomValidationError customValidationError = new CustomValidationError(LocalDateTime.now().toString(),
        HttpStatus.INTERNAL_SERVER_ERROR.value(), errors, getRequestPath());

    return ResponseEntity.internalServerError().body(customValidationError);
  }

  private String getRequestPath() {
    ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (sra != null) {
      HttpServletRequest request = sra.getRequest();
      if (request != null) {
        return request.getRequestURI();
      }
    }
    return "";

  }
}

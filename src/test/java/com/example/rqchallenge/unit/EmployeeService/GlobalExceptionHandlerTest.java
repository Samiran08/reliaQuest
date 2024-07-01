package com.example.rqchallenge.unit.EmployeeService;

import com.example.rqchallenge.Exception.CustomException;
import com.example.rqchallenge.Exception.GlobalExceptionHandler;
import com.example.rqchallenge.model.CustomValidationError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class GlobalExceptionHandlerTest {

  private GlobalExceptionHandler globalExceptionHandler;

  @BeforeEach
  public void setUp() {
    globalExceptionHandler = new GlobalExceptionHandler();
  }

  @Test
  void testCustomException() {
    CustomException customException = new CustomException("Name is mandatory");
    ResponseEntity<CustomValidationError> responseEntity = globalExceptionHandler.handleCustomException(
        customException);
    assertEquals("Name is mandatory", responseEntity.getBody().getError());
    assertEquals(400, responseEntity.getBody().getStatus());
    assertEquals("", responseEntity.getBody().getPath());

  }

  @Test
  void testException() {
    Exception exception = new Exception("Exception");
    ResponseEntity<CustomValidationError> responseEntity = globalExceptionHandler.handleException(exception);
    assertEquals("Exception", responseEntity.getBody().getError());
    assertEquals(500, responseEntity.getBody().getStatus());
    assertEquals("", responseEntity.getBody().getPath());

  }
}

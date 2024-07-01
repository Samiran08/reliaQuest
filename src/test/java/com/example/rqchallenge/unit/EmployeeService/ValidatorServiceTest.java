package com.example.rqchallenge.unit.EmployeeService;

import com.example.rqchallenge.model.Employee;
import com.example.rqchallenge.model.Error;
import com.example.rqchallenge.service.ValidatorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ValidatorServiceTest {

  private final ValidatorService validatorService = new ValidatorService();

  @Test
  void testWithoutError() {
    Employee employee = new Employee(2, "Employee1", 55000, 25);
    Error error = validatorService.validateEmployee(employee);
    assertTrue(error.messages.isEmpty());
  }

  @Test
  void testWithError() {
    Employee employee = new Employee(2, "", 0, 0);
    Error error = validatorService.validateEmployee(employee);
    assertFalse(error.messages.isEmpty());
  }

}

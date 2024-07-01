package com.example.rqchallenge.integration;


import com.example.rqchallenge.model.Employee;
import com.example.rqchallenge.service.EmployeeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmployeeControllerTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private EmployeeService employeeService;

  private String baseUrl;

  @BeforeEach
  public void setup() {
    baseUrl = "http://localhost:" + port + "/app/employees";
  }

  @AfterEach
  public void destroy() {
    employeeService.clearAllEmployees();
  }

  @Test
  void testCreateEmployee() {
    Employee employee = new Employee();
    employee.setName("Employee0");
    employee.setAge(25);
    employee.setSalary(3000);

    Employee expectedEmployee = new Employee();
    expectedEmployee.setId(0);
    expectedEmployee.setName("Employee0");
    expectedEmployee.setAge(25);
    expectedEmployee.setSalary(3000);

    ResponseEntity<Employee> employeeResponse = restTemplate.postForEntity(baseUrl, employee, Employee.class);
    assertEquals(HttpStatus.OK, employeeResponse.getStatusCode());
    assertEquals(expectedEmployee, employeeResponse.getBody());
  }

  @Test
  void testGetEmployeesByName() {
    createEmployees(11);

    List<Employee> employees = restTemplate.exchange(baseUrl + "/search/Employee1", HttpMethod.GET, null,
        new ParameterizedTypeReference<List<Employee>>() {
        }).getBody();
    assert employees != null;
    assertEquals(2, employees.size());
  }

  @Test
  void testGetEmployeeById() {
    createEmployees(10);
    List<Employee> allEmployees = employeeService.getAllEmployees();
    ResponseEntity<Employee> response = restTemplate.getForEntity(baseUrl + "/5", Employee.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Employee4", response.getBody().getName());
  }

  @Test
  void testGetAllEmployees() {
    createEmployees(10);
    ResponseEntity<List<Employee>> allEmployees = restTemplate.exchange(baseUrl, HttpMethod.GET, null,
        new ParameterizedTypeReference<List<Employee>>() {
        });
    assertEquals(HttpStatus.OK, allEmployees.getStatusCode());
    assertEquals(10, allEmployees.getBody().size());
  }

  @Test
  void testDeleteEmployee() {
    createEmployees(2);
    String response = restTemplate.exchange(baseUrl + "/1", HttpMethod.DELETE, null, String.class).getBody();
    assertEquals("Employee0", response);
  }

  @Test
  void testGetHighestSalaryOfEmployees() {
    createEmployees(10);
    ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/highestSalary", String.class);
    int highestSalary = Integer.parseInt(String.valueOf(response.getBody()));
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(10000, highestSalary);
  }

  @Test
  void testGetTopTenHighestEarningEmployeeNames() {
    createEmployees(15);
    ResponseEntity<List<String>> topTenHighestSalaryEmployees = restTemplate.exchange(
        baseUrl + "/topTenHighestEarningEmployeeNames", HttpMethod.GET, null,
        new ParameterizedTypeReference<List<String>>() {
        });
    List<String> highestEmployeeNames = new ArrayList<>();
    for (int i = 5; i < 15; i++) {
      highestEmployeeNames.add("Employee" + i);
    }
    assertEquals(HttpStatus.OK, topTenHighestSalaryEmployees.getStatusCode());
    assertTrue(highestEmployeeNames.containsAll(Objects.requireNonNull(topTenHighestSalaryEmployees.getBody())));
  }

  @Test
  void testGetHighestEarningEmployeeNames() {
    createEmployees(15);
    ResponseEntity<List<String>> topTenHighestSalaryEmployees = restTemplate.exchange(
        baseUrl + "/topEarningEmployeeNames?noOfEmployees=5", HttpMethod.GET, null,
        new ParameterizedTypeReference<List<String>>() {
        });
    List<String> highestEmployeeNames = new ArrayList<>();
    for (int i = 10; i < 15; i++) {
      highestEmployeeNames.add("Employee" + i);
    }
    assertEquals(HttpStatus.OK, topTenHighestSalaryEmployees.getStatusCode());
    assertTrue(highestEmployeeNames.containsAll(Objects.requireNonNull(topTenHighestSalaryEmployees.getBody())));
  }

  private void createEmployees(int noOfEmployees) {
    List<Employee> employeeList = new ArrayList<>();
    for (int i = 0; i < noOfEmployees; i++) {
      Employee employee = new Employee();
      employee.setName("Employee" + i);
      employee.setAge(20 + i);
      employee.setSalary(1000 * (i + 1));
      employeeService.createEmployee(employee);
    }
  }

}

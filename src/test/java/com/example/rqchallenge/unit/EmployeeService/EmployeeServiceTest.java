package com.example.rqchallenge.unit.EmployeeService;

import com.example.rqchallenge.Exception.CustomException;
import com.example.rqchallenge.model.Employee;
import com.example.rqchallenge.model.Error;
import com.example.rqchallenge.service.EmployeeService;
import com.example.rqchallenge.service.ValidatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class EmployeeServiceTest {

  @InjectMocks
  private EmployeeService employeeService;

  @Mock
  private ValidatorService validatorService;

  private Map<Integer, Employee> employeeData;

  @BeforeEach
  public void init() {
    employeeData = new ConcurrentHashMap<>();
    employeeService = new EmployeeService(employeeData, validatorService);
  }

  @Test
  void testCreateEmployee() {
    Employee employee = new Employee();
    employee.setName("Samiran");
    employee.setAge(22);
    employee.setSalary(100);
    when(validatorService.validateEmployee(employee)).thenReturn(new Error());
    Employee employee1 = employeeService.createEmployee(employee);
    verify(validatorService, times(1)).validateEmployee(employee);
    assertTrue(employee1.getId() > 0);
  }

  @Test
  void testCreateEmployeeWithoutName() {
    Employee employee = new Employee();
    employee.setAge(22);
    employee.setSalary(100);
    when(validatorService.validateEmployee(employee)).thenReturn(new Error(List.of("Name is mandatory")));
    CustomException customException = assertThrows(CustomException.class,
        () -> employeeService.createEmployee(employee));
    assertEquals("Name is mandatory", customException.getMessage());
  }

  @Test
  void testCreateEmployeeWithoutNameAndSalary() {
    Employee employee = new Employee();
    employee.setAge(22);
    when(validatorService.validateEmployee(employee)).thenReturn(
        new Error(List.of("Name is mandatory", "Please provide valid salary")));
    CustomException customException = assertThrows(CustomException.class,
        () -> employeeService.createEmployee(employee));
    assertEquals("Name is mandatory, Please provide valid salary", customException.getMessage());
  }

  @Test
  void testGetAllEmployee() {
    populateEmployeesData(5);
    assertEquals(5, employeeService.getAllEmployees().size());
  }

  @Test
  void testGetEmployeeWithNameSearch() {
    populateEmployeesData(12);
    List<Employee> employee = employeeService.getEmployeesByNameSearch("Employee1");
    assertEquals(3, employee.size());
  }

  @Test
  void testGetEmployeeWithNameSearchForEmptyData() {
    CustomException ex = assertThrows(CustomException.class,
        () -> employeeService.getEmployeesByNameSearch("Employee1"));
    assertEquals("No data found for employees", ex.getMessage());
  }

  @Test
  void testGetEmployeeWithNameSearchForInvalidSearch() {
    populateEmployeesData(1);
    List<Employee> employee = employeeService.getEmployeesByNameSearch("Employee1");
    assertEquals(0, employee.size());
  }

  @Test
  void testGetEmployeeById() {
    populateEmployeesData(5);
    Employee employee = employeeService.get(String.valueOf(1));
    Employee expectedEmployee = new Employee();
    expectedEmployee.setId(1);
    expectedEmployee.setName("Employee1");
    expectedEmployee.setAge(21);
    expectedEmployee.setSalary(2000);
    assertEquals(expectedEmployee, employee);
  }


  @Test
  void testGetEmployeeByIdForInvalidSearch() {
    populateEmployeesData(5);
    CustomException ex = assertThrows(CustomException.class, () -> employeeService.get("7"));
    assertEquals("Employee with id 7 not found", ex.getMessage());
  }

  @Test
  void testDelete() {
    populateEmployeesData(5);
    String name = employeeService.delete("2");
    assertEquals("Employee2", name);
  }

  @Test
  void testDeleteForInvalidId() {
    populateEmployeesData(5);
    CustomException ex = assertThrows(CustomException.class, () -> employeeService.delete("7"));
    assertEquals("Employee with id 7 not found", ex.getMessage());
  }

  @Test
  void testGetHighestSalary() {
    //TODO add for no Employees
    populateEmployeesData(5);
    Integer salary = employeeService.getHighestSalary();
    assertEquals(5000, salary);
  }

  @Test
  void testTopTenHighestEarningEmployeeNames() {
    populateEmployeesData(15);
    List<String> employeeNames = employeeService.getTopEarningEmployeeNames(10);
    List<String> highestEmployeeNames = new ArrayList<>();
    for (int i = 5; i < 15; i++) {
      highestEmployeeNames.add("Employee" + i);
    }
    assertTrue(employeeNames.containsAll(highestEmployeeNames));
  }

  @Test
  void testTopTenHighestEarningEmployeeNamesWithSmallList() {
    populateEmployeesData(7);
    List<String> employeeNames = employeeService.getTopEarningEmployeeNames(10);
    List<String> highestEmployeeNames = new ArrayList<>();
    for (int i = 0; i < 7; i++) {
      highestEmployeeNames.add("Employee" + i);
    }
    assertTrue(employeeNames.containsAll(highestEmployeeNames));
  }

  private void populateEmployeesData(int noOfEmployees) {
    for (int i = 0; i < noOfEmployees; i++) {
      Employee employee = new Employee();
      employee.setId(i);
      employee.setName("Employee" + i);
      employee.setAge(20 + i);
      employee.setSalary(1000 * (i + 1));
      employeeData.put(i, employee);
    }
  }
}

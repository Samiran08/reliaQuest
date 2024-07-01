package com.example.rqchallenge.controller;

import com.example.rqchallenge.employees.IEmployeeController;
import com.example.rqchallenge.model.Employee;
import com.example.rqchallenge.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EmployeeController implements IEmployeeController {

  private final EmployeeService employeeService;

  @Override
  public ResponseEntity<List<Employee>> getAllEmployees() {
    return ResponseEntity.ok(employeeService.getAllEmployees());
  }

  @Override
  public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
    return ResponseEntity.ok(employeeService.getEmployeesByNameSearch(searchString));
  }

  @Override
  public ResponseEntity<Employee> getEmployeeById(String id) {
    return ResponseEntity.ok(employeeService.get(id));
  }

  @Override
  public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
    return ResponseEntity.ok(employeeService.getHighestSalary());
  }

  @Override
  public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
    return ResponseEntity.ok(employeeService.getTopEarningEmployeeNames(10));
  }

  @Override
  public ResponseEntity<List<String>> getTopEarningEmployeeNames(int noOfEmployees) {
    return ResponseEntity.ok(employeeService.getTopEarningEmployeeNames(noOfEmployees));
  }

  @Override
  public ResponseEntity<Employee> createEmployee(Employee employeeInput) {
    return ResponseEntity.ok(employeeService.createEmployee(employeeInput));
  }

  @Override
  public ResponseEntity<String> deleteEmployeeById(String id) {
    return ResponseEntity.ok(employeeService.delete(id));
  }
}

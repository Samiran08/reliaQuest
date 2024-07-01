package com.example.rqchallenge.employees;

import com.example.rqchallenge.model.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public interface IEmployeeController {

  @GetMapping()
  ResponseEntity<List<Employee>> getAllEmployees() throws IOException;

  @GetMapping("/search/{searchString}")
  ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable String searchString);

  @GetMapping("/{id}")
  ResponseEntity<Employee> getEmployeeById(@PathVariable String id);

  @GetMapping("/highestSalary")
  ResponseEntity<Integer> getHighestSalaryOfEmployees();

  @GetMapping("/topTenHighestEarningEmployeeNames")
  @Deprecated(forRemoval = true)
  ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames();

  @GetMapping("/topEarningEmployeeNames")
  ResponseEntity<List<String>> getTopEarningEmployeeNames(@RequestParam(name = "noOfEmployees") int noOfEmployees);

  @PostMapping()
  ResponseEntity<Employee> createEmployee(@RequestBody Employee employeeInput);

  @DeleteMapping("/{id}")
  ResponseEntity<String> deleteEmployeeById(@PathVariable String id);

}

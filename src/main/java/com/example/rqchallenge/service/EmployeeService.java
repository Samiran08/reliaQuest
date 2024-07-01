package com.example.rqchallenge.service;

import com.example.rqchallenge.Exception.CustomException;
import com.example.rqchallenge.model.Employee;
import com.example.rqchallenge.model.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmployeeService {

  @Autowired
  private ValidatorService validatorService;
  private Map<Integer, Employee> employeeData = new ConcurrentHashMap<>();
  private final AtomicInteger idSequence = new AtomicInteger(0);

  public EmployeeService(Map<Integer, Employee> employeeData, ValidatorService validatorService) {
    this.employeeData = employeeData;
    this.validatorService = validatorService;
  }

  public Employee createEmployee(Employee employeeInput) {
    Error error = validatorService.validateEmployee(employeeInput);
    if (!CollectionUtils.isEmpty(error.messages)) {
      throw new CustomException(String.join(", ", error.messages));
    }
    int id = idSequence.incrementAndGet();
    employeeInput.setId(id);
    employeeData.put(id, employeeInput);
    log.info("Employee added successfully {}", employeeInput);
    return employeeInput;

  }

  public List<Employee> getAllEmployees() {
    return new ArrayList<>(employeeData.values());
  }

  public List<Employee> getEmployeesByNameSearch(String searchString) {
    if (employeeData.isEmpty()) {
      throw new CustomException("No data found for employees");
    }
    return employeeData.values().stream()
        .filter(employee -> employee.getName().toUpperCase().contains(searchString.toUpperCase()))
        .collect(Collectors.toList());

  }

  public Employee get(String id) {

    Employee employee = employeeData.get(Integer.valueOf(id));
    if (ObjectUtils.isEmpty(employee)) {
      throw new CustomException("Employee with id " + id + " not found");
    }
    return employee;
  }

  public String delete(String id) {
    if (!employeeData.containsKey(Integer.valueOf(id))) {
      throw new CustomException("Employee with id " + id + " not found");
    }
    return employeeData.remove(Integer.valueOf(id)).getName();

  }

  public Integer getHighestSalary() {
    return employeeData.values().stream().max(Comparator.comparingInt(Employee::getSalary))
        .orElseThrow(() -> new CustomException("No data found for employees")).getSalary();
  }

  public List<String> getTopEarningEmployeeNames(int noOfEmployees) {
    return employeeData.values().stream().sorted(Comparator.comparingInt(Employee::getSalary).reversed())
        .limit(noOfEmployees).map(Employee::getName).collect(Collectors.toList());
  }

  public void clearAllEmployees() {
    employeeData.clear();
    idSequence.set(0);
  }
}

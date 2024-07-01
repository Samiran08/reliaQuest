package com.example.rqchallenge.service;

import com.example.rqchallenge.model.Employee;
import com.example.rqchallenge.model.Error;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ValidatorService {

  public Error validateEmployee(Employee employeeInput) {
    Error errors = new Error();
    if (StringUtils.isEmpty(employeeInput.getName())) {
      errors.messages.add("Name is mandatory");
    }
    if (employeeInput.getSalary() <= 0) {
      errors.messages.add("Please provide valid salary");
    }
    if (employeeInput.getAge() <= 0) {
      errors.messages.add("Please provide valid age");
    }
    return errors;
  }
}

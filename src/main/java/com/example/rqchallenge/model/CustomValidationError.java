package com.example.rqchallenge.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonPropertyOrder({"timestamp", "status", "error", "path"})
public class CustomValidationError {

  private String timestamp;
  private int status;
  private String error;
  private String path;
}

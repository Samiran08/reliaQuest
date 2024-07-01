package com.example.rqchallenge.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class Error {

  public List<String> messages = new ArrayList<>();
}

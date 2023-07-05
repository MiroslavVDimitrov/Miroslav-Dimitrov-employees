package com.example.employees.exception;

public class ReadFileException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ReadFileException(String msg) {
    super(msg);
  }
}
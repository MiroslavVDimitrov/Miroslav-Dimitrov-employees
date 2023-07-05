package com.example.employees.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.employees.model.PairEmployee;
import com.example.employees.service.CSVService;

@CrossOrigin("http://localhost:8081")
@RestController
public class CSVController {

  private final CSVService fileService;

  public CSVController(CSVService fileService) {
    this.fileService = fileService;
  }

  @PostMapping("/upload")
  public ResponseEntity<List<PairEmployee>> employeeToJson(@RequestParam("file") MultipartFile file) throws Exception {
    return new ResponseEntity<>(fileService.determinePair(file), HttpStatus.OK);
  }

}

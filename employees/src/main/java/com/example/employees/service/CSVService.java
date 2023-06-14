package com.example.employees.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.employees.helper.CSVHelper;
import com.example.employees.model.PairEmployee;

@Service
public class CSVService {

  // Convert List<PairEmployee> to .CSV and return ResponseEntity<Resource>
  public ByteArrayInputStream load(List<PairEmployee> list) {
    ByteArrayInputStream in = CSVHelper.pairEmployeeToCSV(list);
    return in;
  }

  public List<PairEmployee> convert(MultipartFile file) throws IOException {
    CSVHelper sCsvHelper = new CSVHelper();
    return sCsvHelper.convert(file);
  }

  
}
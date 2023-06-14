package com.example.employees.helper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.springframework.web.multipart.MultipartFile;

import com.example.employees.model.Employee;
import com.example.employees.model.PairEmployee;



public class CSVHelper {
  public static String TYPE = "text/csv";
  static String[] HEADERs = { "EmpID", "ProjectID", "DateFrom", "DateTo" };

  public static boolean hasCSVFormat(MultipartFile file) {

    if (!TYPE.equals(file.getContentType())) {
      return false;
    }

    return true;
  }
 public List<PairEmployee> convert(MultipartFile file) throws IOException {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
                CSVParser csvParser = new CSVParser(fileReader,
                        CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
           
            DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder()
                    .parseCaseInsensitive().parseLenient()
                    .appendPattern("[yyyy-MM-dd]")
                    .appendPattern("[yyyy/MM/dd]")
                    .appendPattern("[M/dd/yyyy]")
                    .appendPattern("[M/d/yyyy]")
                    .appendPattern("[MM/dd/yyyy]")
                    .appendPattern("[MMM dd yyyy]");

            DateTimeFormatter formatter2 = builder.toFormatter(Locale.ENGLISH);
            Map<Integer, List<Employee>> project = new HashMap<>();

            List<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                Employee employee = new Employee();
                employee.setEmpId(Integer.parseInt(csvRecord.get("EmpID")));
                employee.setProjectId(Integer.parseInt(csvRecord.get("ProjectID")));
                employee.setDateFrom(LocalDate.parse(csvRecord.get("DateFrom"), formatter2));
                if (csvRecord.get("DateTo").toUpperCase().equals("NULL")) {
                    employee.setDateTo(LocalDate.now());
                } else {
                    employee.setDateTo(LocalDate.parse(csvRecord.get("DateTo"), formatter2));
                }

                if (!project.containsKey(employee.getProjectId())) {
                    List<Employee> tEmployees = new ArrayList<>();
                    tEmployees.add(employee);
                    project.put(employee.getProjectId(), tEmployees);
                }

                else {
                    project.get(employee.getProjectId()).add(employee);

                }

            }

            List<PairEmployee> pairEmployees = new ArrayList<>();

            project.forEach(
                    (key, value) -> {
                        for (int index = 0; index < value.size(); index++) {
                            Employee emp = value.get(index);

                            for (int j = index + 1; j < value.size(); j++) {
                                LocalDate min;
                                LocalDate max;

                                Employee emp2 = value.get(j);
                        
                                if (emp.getDateFrom().isAfter(emp2.getDateFrom())) {
                                    min = emp.getDateFrom();
                                } else {
                                    min = emp2.getDateFrom();
                                }
                                
                                if (emp.getDateTo().isBefore(emp2.getDateTo())) {
                                    max =  emp.getDateTo();
                                } else {
                                    max = emp2.getDateTo();
                                }

                                if (min.isBefore(max)) {
                                    PairEmployee pairEmployee = new PairEmployee();
                                    pairEmployee.setEmpId1(emp.getEmpId());
                                    pairEmployee.setEmpId2(emp2.getEmpId());
                                    pairEmployee.setProjectId(emp.getProjectId());
                                    pairEmployee.setDaysWorked((int) (ChronoUnit.DAYS.between(min, max)));

                                    pairEmployees.add(pairEmployee);
                                   
                                }

                            }
                        }
                    });      

            return pairEmployees;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }



  public static ByteArrayInputStream pairEmployeeToCSV(List<PairEmployee> employees) {
    final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

    try (ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
      for (PairEmployee employee : employees) {
        List<Object> data = Arrays.asList(
              String.valueOf(employee.getEmpId1()),
              employee.getEmpId2(),
              employee.getProjectId(),
              employee.getDaysWorked()
            );

        csvPrinter.printRecord(data);
      }

      csvPrinter.flush();
      return new ByteArrayInputStream(out.toByteArray());
    } catch (IOException e) {
      throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
    }
  }
  

}

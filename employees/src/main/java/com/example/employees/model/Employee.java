package com.example.employees.model;


import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee{
    Integer empId;
    Integer projectId;
    LocalDate dateFrom;
    LocalDate dateTo;


}

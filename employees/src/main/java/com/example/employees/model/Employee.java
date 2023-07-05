package com.example.employees.model;


import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee{
    int empId;
    int projectId;
    LocalDate dateFrom;
    LocalDate dateTo;


}

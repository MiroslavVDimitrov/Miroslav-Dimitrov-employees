package com.example.employees.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PairEmployee {

    Integer empId1;
    Integer empId2;
    Integer  projectId;
    Integer daysWorked;

    
}
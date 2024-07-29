package org.example.employeeproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO {

    private int id;
    private String name;
    private int parentDepartmentId;
    private String parentDepartmentName;
}

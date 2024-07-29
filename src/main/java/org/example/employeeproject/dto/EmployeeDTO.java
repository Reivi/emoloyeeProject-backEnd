package org.example.employeeproject.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDTO {

    private int employeeId;
    private String fullName;
    private int departmentId;

    public EmployeeDTO(String fullName) {
        this.fullName = fullName;
    }
}

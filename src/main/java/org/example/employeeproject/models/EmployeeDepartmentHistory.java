package org.example.employeeproject.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table( name = "employee_department_history",
        indexes = {
        @Index(name = "idx_department_start_end", columnList = "department_id, start_date, end_date"),
        @Index(name = "idx_employee_start_end", columnList = "employee_id, start_date, end_date")
})
public class EmployeeDepartmentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private Department department;

    private LocalDate startDate;
    private LocalDate endDate;


}

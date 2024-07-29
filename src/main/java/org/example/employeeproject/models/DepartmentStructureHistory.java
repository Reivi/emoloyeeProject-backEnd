package org.example.employeeproject.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table( name = "department_structure_history",
        indexes = {@Index(columnList = "department_id, parent_department_id, start_date, end_date")
})
public class DepartmentStructureHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "parent_department_id", referencedColumnName = "id")
    private Department parentDepartment;

    private LocalDate startDate;
    private LocalDate endDate;
}

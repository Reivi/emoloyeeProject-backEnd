package org.example.employeeproject.repositories;

import org.example.employeeproject.models.Department;
import org.example.employeeproject.models.DepartmentStructureHistory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DepartmentStructureHistoryRepository extends JpaRepository<DepartmentStructureHistory, Integer> {

    @EntityGraph(attributePaths = {"department", "parentDepartment"})
    @Query("SELECT dsh FROM DepartmentStructureHistory dsh WHERE " +
            "(:date BETWEEN dsh.startDate AND COALESCE(dsh.endDate, :date))")
    List<DepartmentStructureHistory> findStructureByDate(@Param("date") LocalDate date);


    @EntityGraph(attributePaths = {"department", "parentDepartment"})
    @Query("SELECT dsh FROM DepartmentStructureHistory dsh WHERE dsh.department.id = :departmentId AND dsh.endDate IS NULL")
    DepartmentStructureHistory findCurrentParentStructure(@Param("departmentId") int departmentId);

    @EntityGraph(attributePaths = {"department"})
    @Query("SELECT DISTINCT dsh.department FROM DepartmentStructureHistory dsh WHERE dsh.endDate IS NULL")
    List<Department> findCurrentDepartments();
}

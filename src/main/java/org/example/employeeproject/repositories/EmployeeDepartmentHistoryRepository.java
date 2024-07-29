package org.example.employeeproject.repositories;

import org.example.employeeproject.models.Department;
import org.example.employeeproject.models.Employee;
import org.example.employeeproject.models.EmployeeDepartmentHistory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeDepartmentHistoryRepository extends JpaRepository<EmployeeDepartmentHistory, Integer> {

    @EntityGraph(attributePaths = {"employee", "department"})
    @Query("SELECT DISTINCT edh FROM EmployeeDepartmentHistory edh WHERE " +
            "edh.department.id = :departmentId AND " +
            "((edh.startDate <= :endDate AND (edh.endDate IS NULL OR edh.endDate >= :startDate)) " +
            "OR (edh.startDate BETWEEN :startDate AND :endDate) " +
            "OR (edh.endDate BETWEEN :startDate AND :endDate))")
    List<EmployeeDepartmentHistory> findEmployeesByDepartmentAndPeriod(@Param("departmentId") int departmentId,
                                                                       @Param("startDate") LocalDate startDate,
                                                                       @Param("endDate") LocalDate endDate);

    List<EmployeeDepartmentHistory> findByEmployeeAndEndDateIsNull(Employee employee);
}

package org.example.employeeproject.service;

import jakarta.transaction.Transactional;
import org.example.employeeproject.dto.EmployeeDTO;
import org.example.employeeproject.models.Department;
import org.example.employeeproject.models.Employee;
import org.example.employeeproject.models.EmployeeDepartmentHistory;
import org.example.employeeproject.repositories.DepartmentRepository;
import org.example.employeeproject.repositories.EmployeeDepartmentHistoryRepository;
import org.example.employeeproject.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeDepartmentHistoryRepository employeeDepartmentHistoryRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public EmployeeService(EmployeeDepartmentHistoryRepository employeeDepartmentHistoryRepository, EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeDepartmentHistoryRepository = employeeDepartmentHistoryRepository;
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<EmployeeDepartmentHistory> getEmployeesByDepartmentAndPeriod(int departmentId, LocalDate startDate, LocalDate endDate) {
        return employeeDepartmentHistoryRepository.findEmployeesByDepartmentAndPeriod(departmentId, startDate, endDate);
    }

    @Transactional
    public Employee createEmployee(EmployeeDTO EmployeeDTO) {
        Employee employee = new Employee();
        employee.setFullName(EmployeeDTO.getFullName());
        employeeRepository.save(employee);
        saveInEmployeeHistory(employee, EmployeeDTO);

        return employee;
    }

    @Transactional
    public void transferEmployee(EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(employeeDTO.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("Неверный идентификатор сотрудника"));

        List<EmployeeDepartmentHistory> currentHistories = employeeDepartmentHistoryRepository
                .findByEmployeeAndEndDateIsNull(employee);

        if (currentHistories.isEmpty()) {
            throw new IllegalStateException("Сотрудник не приписан к отделу");
        }

        EmployeeDepartmentHistory currentHistory = currentHistories.get(0);
        currentHistory.setEndDate(LocalDate.now());
        employeeDepartmentHistoryRepository.save(currentHistory);
        saveInEmployeeHistory(employee, employeeDTO);

    }

    private void saveInEmployeeHistory(Employee employee, EmployeeDTO employeeDTO) {
        Department newDepartment = departmentRepository.findById(employeeDTO.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("Неверный идентификатор отдела"));
        
        EmployeeDepartmentHistory newHistory = new EmployeeDepartmentHistory();
        newHistory.setEmployee(employee);
        newHistory.setDepartment(newDepartment);
        newHistory.setStartDate(LocalDate.now());
        employeeDepartmentHistoryRepository.save(newHistory);

    }

    @Transactional
    public void terminateEmployee(int employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Неверный идентификатор сотрудника"));

        List<EmployeeDepartmentHistory> currentHistories = employeeDepartmentHistoryRepository
                .findByEmployeeAndEndDateIsNull(employee);

        if (!currentHistories.isEmpty()) {
            LocalDate today = LocalDate.now();
            for (EmployeeDepartmentHistory history : currentHistories) {
                history.setEndDate(today);
                employeeDepartmentHistoryRepository.save(history);
            }
        }
    }
}

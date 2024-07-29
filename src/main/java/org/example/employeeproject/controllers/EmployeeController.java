package org.example.employeeproject.controllers;

import org.example.employeeproject.dto.EmployeeDTO;
import org.example.employeeproject.models.Employee;
import org.example.employeeproject.models.EmployeeDepartmentHistory;
import org.example.employeeproject.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/create-employee")
    public Employee createEmployee(@RequestBody EmployeeDTO EmployeeDTO) {
        return employeeService.createEmployee(EmployeeDTO);
    }

    @GetMapping("/department/{id}")
    public List<EmployeeDTO> getEmployeesByDepartmentAndPeriod(@PathVariable int id, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        List<EmployeeDepartmentHistory> histories = employeeService.getEmployeesByDepartmentAndPeriod(id, startDate, endDate);
        return histories.stream()
                .map(history -> new EmployeeDTO(history.getEmployee().getFullName()))
                .distinct()
                .collect(Collectors.toList());
    }

    @PostMapping("/transfer")
    public void transferEmployee(@RequestBody EmployeeDTO EmployeeDTO) {
        employeeService.transferEmployee(EmployeeDTO);
    }

    @PostMapping("/terminate/{employeeId}")
    public void terminateEmployee(@PathVariable int employeeId) {
        employeeService.terminateEmployee(employeeId);
    }
}

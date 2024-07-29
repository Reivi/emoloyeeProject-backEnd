package org.example.employeeproject.controllers;

import org.example.employeeproject.dto.DepartmentDTO;
import org.example.employeeproject.models.Department;
import org.example.employeeproject.service.DepartmentService;
import org.example.employeeproject.service.DepartmentStructureHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final DepartmentStructureHistoryService departmentStructureHistoryService;

    @Autowired
    public DepartmentController(DepartmentService departmentService, DepartmentStructureHistoryService departmentStructureHistoryService) {
        this.departmentService = departmentService;
        this.departmentStructureHistoryService = departmentStructureHistoryService;
    }


    @GetMapping("/by-date")
    public List<DepartmentDTO> getDepartmentsByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return departmentStructureHistoryService.getDepartmentStructureByDate(date);
    }

    @PutMapping("/{departmentId}/parent/{parentDepartmentId}")
    public void setParentDepartment( @PathVariable int departmentId, @PathVariable int parentDepartmentId) {
        departmentStructureHistoryService.UpdateParentDepartment(departmentId, parentDepartmentId);
    }

    @GetMapping("/current")
    public List<DepartmentDTO> getCurrentDepartments() {
        return departmentStructureHistoryService.getCurrentDepartments();
    }

    @PostMapping("/create-department")
    public DepartmentDTO addDepartment(@RequestBody DepartmentDTO departmentDTO) {
        return departmentService.addDepartmentWithParent(departmentDTO);
    }
}

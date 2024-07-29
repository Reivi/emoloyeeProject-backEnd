package org.example.employeeproject.service;

import jakarta.transaction.Transactional;
import org.example.employeeproject.dto.DepartmentDTO;
import org.example.employeeproject.models.Department;
import org.example.employeeproject.models.DepartmentStructureHistory;
import org.example.employeeproject.repositories.DepartmentRepository;
import org.example.employeeproject.repositories.DepartmentStructureHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DepartmentService {


    private final DepartmentRepository departmentRepository;
    private final DepartmentStructureHistoryRepository departmentStructureHistoryRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository, DepartmentStructureHistoryRepository departmentStructureHistoryRepository) {
        this.departmentStructureHistoryRepository = departmentStructureHistoryRepository;
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    public DepartmentDTO addDepartmentWithParent(DepartmentDTO departmentDTO) {
        Department newDepartment = new Department();
        newDepartment.setName(departmentDTO.getName());
        departmentRepository.save(newDepartment);

        Department parentDepartment = null;
        if (departmentDTO.getParentDepartmentId() != 0) {
            parentDepartment = departmentRepository.findById(departmentDTO.getParentDepartmentId())
                    .orElseThrow(() -> new IllegalArgumentException("Неверный идентификатор родительского отдела"));
        }

        DepartmentStructureHistory newHistory = new DepartmentStructureHistory();
        newHistory.setDepartment(newDepartment);
        newHistory.setParentDepartment(parentDepartment);
        newHistory.setStartDate(LocalDate.now());
        departmentStructureHistoryRepository.save(newHistory);

        return convertToDTO(newDepartment, parentDepartment);
    }

    private DepartmentDTO convertToDTO(Department department, Department parentDepartment) {
        return new DepartmentDTO(
                department.getId(),
                department.getName(),
                parentDepartment != null ? parentDepartment.getId() : 0,
                parentDepartment != null ? parentDepartment.getName() : null
        );
    }



}

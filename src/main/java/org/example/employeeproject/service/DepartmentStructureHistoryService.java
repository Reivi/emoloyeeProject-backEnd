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
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentStructureHistoryService {

    private final DepartmentStructureHistoryRepository departmentStructureHistoryRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentStructureHistoryService(DepartmentStructureHistoryRepository departmentStructureHistoryRepository, DepartmentRepository departmentRepository) {
        this.departmentStructureHistoryRepository = departmentStructureHistoryRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<DepartmentDTO> getDepartmentStructureByDate(LocalDate date) {
        List<DepartmentStructureHistory> departments = departmentStructureHistoryRepository.findStructureByDate(date);
        return departments.stream()
                .map(departmentStructureHistory -> {
                    DepartmentDTO dto = new DepartmentDTO();
                    dto.setId(departmentStructureHistory.getDepartment().getId());
                    dto.setName(departmentStructureHistory.getDepartment().getName());
                    if (departmentStructureHistory.getParentDepartment() != null) {
                        dto.setParentDepartmentId(departmentStructureHistory.getParentDepartment().getId());
                        dto.setParentDepartmentName(departmentStructureHistory.getParentDepartment().getName());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void UpdateParentDepartment(int departmentId, Integer newParentDepartmentId) {
        DepartmentStructureHistory currentHistory = departmentStructureHistoryRepository
                .findCurrentParentStructure(departmentId);

        if (currentHistory != null) {
            currentHistory.setEndDate(LocalDate.now());
            departmentStructureHistoryRepository.save(currentHistory);
        }

        if (newParentDepartmentId != null) {
            Department newParentDepartment = departmentRepository.findById(newParentDepartmentId)
                    .orElseThrow(() -> new IllegalArgumentException("Неверный идентификатор родительского отдела"));

            DepartmentStructureHistory newHistory = new DepartmentStructureHistory();
            newHistory.setDepartment(departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new IllegalArgumentException("Неверный идентификатор отдела")));
            newHistory.setParentDepartment(newParentDepartment);
            newHistory.setStartDate(LocalDate.now());
            newHistory.setEndDate(null);

            departmentStructureHistoryRepository.save(newHistory);
        }
    }

    public List<DepartmentDTO> getCurrentDepartments() {
        List<Department> departments = departmentStructureHistoryRepository.findCurrentDepartments();
        return departments.stream()
                .map(department -> {
                    DepartmentDTO dto = new DepartmentDTO();
                    dto.setId(department.getId());
                    dto.setName(department.getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}

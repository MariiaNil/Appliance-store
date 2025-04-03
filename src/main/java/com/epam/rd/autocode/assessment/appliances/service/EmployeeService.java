package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.dto.EmployeeDTO;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {
    Employee createEmployee(Employee employee);
    EmployeeDTO getEmployeeById(Long id);
    Page<EmployeeDTO> getEmployees(Pageable pageable);
    void deleteEmployee(Long id);
    Page<EmployeeDTO> searchEmployees(String search, Pageable pageable);
    List<EmployeeDTO> getAllEmployeesList();
    void hashExistingPasswords();
}

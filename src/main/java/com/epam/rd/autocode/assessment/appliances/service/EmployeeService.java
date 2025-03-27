package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.dto.EmployeeDTO;
import com.epam.rd.autocode.assessment.appliances.model.Employee;

import java.util.List;

public interface EmployeeService {
    public Employee createEmployee(Employee employee);
    public EmployeeDTO getEmployeeById(Long id);
    public List<EmployeeDTO> getEmployees();
    public void deleteEmployee(Long id);
}

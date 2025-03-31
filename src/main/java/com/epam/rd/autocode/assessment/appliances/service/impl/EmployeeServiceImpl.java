package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.dto.EmployeeDTO;
import com.epam.rd.autocode.assessment.appliances.mapper.EmployeeDTOMapper;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.repository.EmployeeRepository;
import com.epam.rd.autocode.assessment.appliances.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    private final EmployeeRepository employeeRepository;
    private final EmployeeDTOMapper employeeDTOMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Employee createEmployee(Employee employee) {
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        logger.info("Creating employee: {}", employee);
        return employeeRepository.save(employee);
    }

    @Override
    public EmployeeDTO getEmployeeById(Long id) {
        logger.info("Getting employee by ID: {}", id);
        return employeeRepository.findById(id)
                .map(employeeDTOMapper)
                .orElseThrow(() -> {
                    logger.error("Employee with ID {} not found", id);
                    return new RuntimeException("Employee not found");
                });
    }

    @Override
    public Page<EmployeeDTO> getEmployees(Pageable pageable) {
        Page<Employee> employeePage = employeeRepository.findAll(pageable);
        logger.info("Employees found: {}", employeePage.getTotalElements());
        return employeePage.map(employeeDTOMapper);
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
        logger.info("Employee with ID {} deleted", id);
    }

    @Override
    public Page<EmployeeDTO> searchEmployees(String search, Pageable pageable) {
        logger.info("Searching employees by name: {}", search);
        return employeeRepository.findByNameContainingIgnoreCase(search, pageable)
                .map(employeeDTOMapper);
    }

    @Override
    public List<EmployeeDTO> getAllEmployeesList() {
        logger.info("Getting all employees");
        return employeeRepository.findAll()
                .stream()
                .map(employeeDTOMapper)
                .toList();
    }

    @Override
    @Transactional
    public void hashExistingPasswords() {
        logger.info("Hashing existing passwords");
        Pageable pageable = PageRequest.of(0, 5);
        Page<Employee> employeePage;

        do {
            employeePage = employeeRepository.findAll(pageable);
            List<Employee> employeesToUpdate = new ArrayList<>();
            for (Employee employee : employeePage.getContent()) {
                String rawPassword = employee.getPassword();
                if (!rawPassword.startsWith("$2a$") && !rawPassword.startsWith("$2b$")) {
                    String hashedPassword = passwordEncoder.encode(rawPassword);
                    employee.setPassword(hashedPassword);
                    employeesToUpdate.add(employee);
                }
            }
            employeeRepository.saveAll(employeesToUpdate);
            pageable = pageable.next();
        } while (employeePage.hasNext());
    }
}

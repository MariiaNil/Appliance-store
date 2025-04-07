package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.dto.EmployeeDTO;
import com.epam.rd.autocode.assessment.appliances.exception.EmployeeNotFoundException;
import com.epam.rd.autocode.assessment.appliances.mapper.EmployeeDTOMapper;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.repository.EmployeeRepository;
import com.epam.rd.autocode.assessment.appliances.service.EmployeeService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeDTOMapper employeeDTOMapper;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;

    @Override
    @Transactional
    @PreAuthorize("hasRole('EMPLOYEE')")
    public Employee createEmployee(Employee employee) {
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        return employeeRepository.save(employee);
    }

    @Override
    /*@PreAuthorize("hasRole('EMPLOYEE')")*/
    public EmployeeDTO getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .map(employeeDTOMapper)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee not found"));
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYEE')")
    public Page<EmployeeDTO> getEmployees(Pageable pageable) {
        Page<Employee> employeePage = employeeRepository.findAll(pageable);
        return employeePage.map(employeeDTOMapper);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('EMPLOYEE')")
    public void deleteEmployee(Long id) {
        Employee employee = entityManager.find(Employee.class, id);
        List<Orders> orders = entityManager.createQuery(
                "SELECT o FROM Orders o WHERE o.employee = :employee", Orders.class
        ).setParameter("employee", employee).getResultList();

        for (Orders order : orders) {
            order.setEmployee(null);
            entityManager.merge(order);
        }
        entityManager.remove(employee);
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYEE')")
    public Page<EmployeeDTO> searchEmployees(String search, Pageable pageable) {
        return employeeRepository.findByNameContainingIgnoreCase(search, pageable)
                .map(employeeDTOMapper);
    }

    @Override
    /*@PreAuthorize("hasRole('EMPLOYEE')")*/
    public List<EmployeeDTO> getAllEmployeesList() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeDTOMapper)
                .toList();
    }

    @Override
    @Transactional
    public void hashExistingPasswords() {
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

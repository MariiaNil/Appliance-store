package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.dto.EmployeeDTO;
import com.epam.rd.autocode.assessment.appliances.exception.EmployeeNotFoundException;
import com.epam.rd.autocode.assessment.appliances.mapper.EmployeeDTOMapper;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.repository.EmployeeRepository;
import com.epam.rd.autocode.assessment.appliances.service.impl.EmployeeServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private EmployeeDTOMapper employeeDTOMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private EmployeeServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEmployee() {
        Employee employee = new Employee();
        employee.setPassword("raw");
        when(passwordEncoder.encode("raw")).thenReturn("encoded");
        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee created = service.createEmployee(employee);
        assertEquals("encoded", employee.getPassword());
        verify(employeeRepository).save(employee);
    }

    @Test
    void testGetEmployeeByIdFound() {
        Long id = 1L;
        Employee employee = new Employee();
        EmployeeDTO dto = new EmployeeDTO(1L, "TestEmployee", "employee@example.com", "TestDepartment", "TestDepartment");
        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));
        when(employeeDTOMapper.apply(employee)).thenReturn(dto);

        EmployeeDTO result = service.getEmployeeById(id);
        assertEquals(dto, result);
    }

    @Test
    void testGetEmployeeByIdNotFound() {
        Long id = 1L;
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EmployeeNotFoundException.class, () -> service.getEmployeeById(id));
    }

    @Test
    void testGetEmployees() {
        Pageable pageable = PageRequest.of(0, 5);
        Employee employee = new Employee();
        EmployeeDTO dto = new EmployeeDTO(1L, "TestEmployee", "employee@example.com", "TestDepartment", "TestDepartment");
        Page<Employee> page = new PageImpl<>(Collections.singletonList(employee));

        when(employeeRepository.findAll(pageable)).thenReturn(page);
        when(employeeDTOMapper.apply(employee)).thenReturn(dto);

        Page<EmployeeDTO> result = service.getEmployees(pageable);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testDeleteEmployee() {
        Long id = 1L;
        Employee employee = new Employee();
        when(entityManager.find(Employee.class, id)).thenReturn(employee);

        TypedQuery<Orders> query = mock(TypedQuery.class);
        when(entityManager.createQuery("SELECT o FROM Orders o WHERE o.employee = :employee", Orders.class))
                .thenReturn(query);
        when(query.setParameter("employee", employee)).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.singletonList(new Orders()));

        service.deleteEmployee(id);
        verify(entityManager).remove(employee);
    }


    @Test
    void testSearchEmployees() {
        String search = "Anna";
        Pageable pageable = PageRequest.of(0, 5);
        Employee employee = new Employee();
        EmployeeDTO dto = new EmployeeDTO(1L, "TestEmployee", "employee@example.com", "TestDepartment", "TestDepartment");
        Page<Employee> page = new PageImpl<>(Collections.singletonList(employee));

        when(employeeRepository.findByNameContainingIgnoreCase(search, pageable)).thenReturn(page);
        when(employeeDTOMapper.apply(employee)).thenReturn(dto);

        Page<EmployeeDTO> result = service.searchEmployees(search, pageable);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testGetAllEmployeesList() {
        Employee employee = new Employee();
        EmployeeDTO dto = new EmployeeDTO(1L, "TestEmployee", "employee@example.com", "TestDepartment", "TestDepartment");
        when(employeeRepository.findAll()).thenReturn(Collections.singletonList(employee));
        when(employeeDTOMapper.apply(employee)).thenReturn(dto);

        java.util.List<EmployeeDTO> list = service.getAllEmployeesList();
        assertEquals(1, list.size());
    }
}

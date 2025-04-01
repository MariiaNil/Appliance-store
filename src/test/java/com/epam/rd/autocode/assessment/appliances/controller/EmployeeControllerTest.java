package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.dto.EmployeeDTO;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @Mock
    private Model model;

    @Mock
    private Pageable pageable;

    @Mock
    private Page<EmployeeDTO> employeePage;

    @InjectMocks
    private EmployeeController controller;

    @Test
    void testListEmployeesWithoutSearch() {
        when(employeeService.getEmployees(any(Pageable.class))).thenReturn(employeePage);

        String view = controller.listEmployees(model, null, pageable);

        verify(employeeService).getEmployees(pageable);
        verify(model).addAttribute("employeePage", employeePage);
        verify(model).addAttribute("search", null);
        assertEquals("employee/employees", view);
    }

    @Test
    void testListEmployeesWithSearch() {
        String search = "Doe";
        when(employeeService.searchEmployees(eq(search), any(Pageable.class))).thenReturn(employeePage);

        String view = controller.listEmployees(model, search, pageable);

        verify(employeeService).searchEmployees(eq(search), eq(pageable));
        verify(model).addAttribute("employeePage", employeePage);
        verify(model).addAttribute("search", search);
        assertEquals("employee/employees", view);
    }

    @Test
    void testShowAddEmployeeForm() {
        String view = controller.showAddEmployeeForm(model);

        verify(model).addAttribute(eq("employee"), any(Employee.class));
        assertEquals("employee/newEmployee", view);
    }

    @Test
    void testAddEmployee() {
        Employee employee = new Employee();
        String view = controller.addEmployee(employee);

        verify(employeeService).createEmployee(employee);
        assertEquals("redirect:/employees", view);
    }

    @Test
    void testDeleteEmployee() {
        Long id = 1L;
        String view = controller.deleteEmployee(id);

        verify(employeeService).deleteEmployee(id);
        assertEquals("redirect:/employees", view);
    }
}

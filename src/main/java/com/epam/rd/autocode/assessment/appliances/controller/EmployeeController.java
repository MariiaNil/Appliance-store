package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.dto.EmployeeDTO;
import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    private final EmployeeService employeeService;

    @GetMapping
    public String listEmployees(
            Model model,
            @RequestParam(value = "search", required = false) String search,
            @PageableDefault(size = 5, sort = {"id", "name", "email", "department"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<EmployeeDTO> employeePage;
        if (search != null && !search.trim().isEmpty())
            employeePage = employeeService.searchEmployees(search, pageable);
        else
            employeePage = employeeService.getEmployees(pageable);
        model.addAttribute("employeePage", employeePage);
        model.addAttribute("search", search);
        logger.info("Employees list successfully loaded, returning view 'employee/employees'");
        return "employee/employees";
    }

    @GetMapping("/add")
    public String showAddEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        logger.info("Showing add employee form");
        return "employee/newEmployee";
    }

    @PostMapping("/add-employee")
    public String addEmployee(@ModelAttribute Employee employee) {
        employeeService.createEmployee(employee);
        logger.info("Employee created successfully");
        return "redirect:/employees";
    }

    @GetMapping("/{id}/delete")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        logger.info("Employee deleted successfully");
        return "redirect:/employees";
    }
}

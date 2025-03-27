package com.epam.rd.autocode.assessment.appliances.dto;

public record EmployeeDTO(
        Long id,
        String name,
        String email,
        String password,
        String department
) { }

package com.epam.rd.autocode.assessment.appliances.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmployeeDto {
    private Long id;
    @NotBlank(message = "{validation.employee.name.notblank}")
    private String name;
    @Email(message = "{validation.employee.email.email}")
    private String email;
    private String department;

}

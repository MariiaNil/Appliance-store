package com.epam.rd.autocode.assessment.appliances.dto;

import com.epam.rd.autocode.assessment.appliances.password.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClientDTO(
        Long id,
        @NotBlank(message = "Name cannot be empty")
        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
        String name,
        @Email(message = "Invalid email format")
        String email,
        @ValidPassword
        String password,
        @Pattern(regexp = "\\d{4}-\\d{4}", message = "Invalid card format")
        String card
) { }

package com.epam.rd.autocode.assessment.appliances.dto;

import com.epam.rd.autocode.assessment.appliances.password.ValidPassword;
import jakarta.validation.constraints.Email;

public record ClientDTO(
        Long id,
        String name,
        @Email
        String email,
        @ValidPassword
        String password,
        String card
) { }

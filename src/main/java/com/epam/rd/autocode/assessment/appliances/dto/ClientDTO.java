package com.epam.rd.autocode.assessment.appliances.dto;

public record ClientDTO(
        Long id,
        String name,
        String email,
        String password,
        String card
) { }

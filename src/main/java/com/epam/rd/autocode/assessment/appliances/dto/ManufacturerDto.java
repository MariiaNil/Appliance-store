package com.epam.rd.autocode.assessment.appliances.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ManufacturerDto {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;
}

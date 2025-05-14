package com.epam.rd.autocode.assessment.appliances.dto;

import com.epam.rd.autocode.assessment.appliances.model.PowerType;

import java.math.BigDecimal;

public record ApplianceDTO(
        Long id,
        String name,
        CategoryDTO category,
        String model,
        ManufacturerDTO manufacturer,
        PowerType powerType,
        String characteristic,
        String description,
        Integer power,
        BigDecimal price,
        String imageUrl
        )
{ }

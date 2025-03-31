package com.epam.rd.autocode.assessment.appliances.dto;

import com.epam.rd.autocode.assessment.appliances.model.Category;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.model.PowerType;

import java.math.BigDecimal;

public record ApplianceDTO(
        Long id,
        String name,
        Category category,
        String model,
        ManufacturerDTO manufacturer,
        PowerType powerType,
        String characteristic,
        String description,
        Integer power,
        BigDecimal price
        )
{ }

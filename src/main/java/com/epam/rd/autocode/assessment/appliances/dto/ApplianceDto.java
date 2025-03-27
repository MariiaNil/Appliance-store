package com.epam.rd.autocode.assessment.appliances.dto;

import com.epam.rd.autocode.assessment.appliances.model.Category;
import com.epam.rd.autocode.assessment.appliances.model.PowerType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ApplianceDto {

    @NotBlank(message = "{validation.appliance.name.notblank}")
    private Long id;
    private String name;
    private Category category;
    private String model;
    private Long manufacturerId;
    private PowerType powerType;
    private String characteristic;
    private String description;
    private Integer power;
    @DecimalMin(value = "0.0", message = "{validation.appliance.price.min}")
    private BigDecimal price;
}

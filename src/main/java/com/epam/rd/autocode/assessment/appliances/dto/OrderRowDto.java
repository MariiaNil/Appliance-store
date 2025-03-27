package com.epam.rd.autocode.assessment.appliances.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderRowDto {
    private Long id;
    private Long applianceId;
    @Min(value = 1, message = "{validation.orderRow.number.min}")
    private Long number;
    @DecimalMin(value = "0.0", message = "{validation.orderRow.amount.min}")
    private BigDecimal amount;
}

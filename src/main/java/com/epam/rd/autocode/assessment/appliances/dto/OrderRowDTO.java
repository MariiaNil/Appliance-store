package com.epam.rd.autocode.assessment.appliances.dto;

import java.math.BigDecimal;

public record OrderRowDTO(
        Long id,
        ApplianceDTO appliance,
        Long number,
        BigDecimal amount
) { }

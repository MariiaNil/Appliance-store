package com.epam.rd.autocode.assessment.appliances.dto;

import java.math.BigDecimal;
import java.util.Set;

public record OrdersDTO(
        Long id,
        ClientDTO client,
        EmployeeDTO employee,
        Set<Long> orderRowIds,
        Boolean approved,
        BigDecimal totalAmount
) { }

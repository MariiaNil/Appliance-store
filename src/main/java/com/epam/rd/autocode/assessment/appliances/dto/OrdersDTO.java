package com.epam.rd.autocode.assessment.appliances.dto;

import java.util.Set;

public record OrdersDTO(
    Long id,
    Long clientId,
    Long employeeId,
    Set<Long> orderRowIds,
    Boolean approved
) { }

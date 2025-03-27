package com.epam.rd.autocode.assessment.appliances.dto;

import lombok.Data;

import java.util.Set;

@Data
public class OrdersDto {
    private Long id;
    private EmployeeDto employeeDto;
    private ClientDto clientDto;
    private Set<OrderRowDto> orderRowDtoSet;
    private Boolean approved;
}

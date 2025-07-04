package com.epam.rd.autocode.assessment.appliances.mapper;

import com.epam.rd.autocode.assessment.appliances.dto.OrderRowDTO;
import com.epam.rd.autocode.assessment.appliances.dto.OrdersDTO;
import com.epam.rd.autocode.assessment.appliances.model.OrderRow;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrdersDTOMapper implements Function<Orders, OrdersDTO> {
    @Override
    public OrdersDTO apply(Orders orders) {
        Set<OrderRowDTO> orderRows = orders.getOrderRowSet()
                .stream()
                .map(this::mapOrderRow)
                .collect(Collectors.toSet());

        BigDecimal totalAmount = orderRows.stream()
                .map(OrderRowDTO::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new OrdersDTO(
                orders.getId(),
                Optional.ofNullable(orders.getClient())
                        .map(new ClientDTOMapper())
                        .orElse(null),
                /*new ClientDTOMapper().apply(orders.getClient()),*/
                Optional.ofNullable(orders.getEmployee())
                        .map(new EmployeeDTOMapper())
                        .orElse(null),
                /*new EmployeeDTOMapper().apply(orders.getEmployee()),*/
                orderRows,
                /*orders.getOrderRowSet().stream()
                        .map(OrderRow::getId)
                        .collect(Collectors.toSet()),*/
                orders.getApproved(),
                totalAmount
        );
    }

    private OrderRowDTO mapOrderRow(OrderRow orderRow) {
        return new OrderRowDTO(
                orderRow.getId(),
                new ApplianceDTOMapper().apply(orderRow.getAppliance()),
                orderRow.getNumber(),
                orderRow.getAmount()
        );
    }
}

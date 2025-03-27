package com.epam.rd.autocode.assessment.appliances.mapper;

import com.epam.rd.autocode.assessment.appliances.dto.OrdersDTO;
import com.epam.rd.autocode.assessment.appliances.model.OrderRow;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrdersDTOMapper implements Function<Orders, OrdersDTO> {
    @Override
    public OrdersDTO apply(Orders orders) {
        return new OrdersDTO(
                orders.getId(),
                orders.getClient().getId(),
                orders.getEmployee().getId(),
                orders.getOrderRowSet().stream()
                        .map(OrderRow::getId)
                        .collect(Collectors.toSet()),
                orders.getApproved()
        );
    }
}

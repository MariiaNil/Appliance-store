package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.dto.OrdersDTO;
import com.epam.rd.autocode.assessment.appliances.model.OrderRow;
import com.epam.rd.autocode.assessment.appliances.model.Orders;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<OrdersDTO> getAllOrders();
    Orders createOrder(Orders orders);
    Optional<OrdersDTO> getOrderById(Long id);
    OrdersDTO updateOrder(Long id, OrdersDTO ordersDto);
    void deleteOrder(Long id);
    Orders addOrderRow(Long id, OrderRow orderRow);
}

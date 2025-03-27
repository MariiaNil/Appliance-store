package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.dto.OrdersDTO;
import com.epam.rd.autocode.assessment.appliances.model.OrderRow;
import com.epam.rd.autocode.assessment.appliances.model.Orders;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    public List<OrdersDTO> getAllOrders();
    public Orders createOrder(Orders orders);
    public Optional<OrdersDTO> getOrderById(Long id);
    public OrdersDTO updateOrder(Long id, OrdersDTO ordersDto);
    public void deleteOrder(Long id);
    public Orders addOrderRow(Long id, OrderRow orderRow);
}

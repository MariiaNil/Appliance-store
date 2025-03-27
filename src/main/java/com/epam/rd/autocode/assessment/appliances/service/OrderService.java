package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.model.OrderRow;
import com.epam.rd.autocode.assessment.appliances.model.Orders;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    public List<Orders> getAllOrders();
    public Orders createOrder(Orders orders);
    public Optional<Orders> getOrderById(Long id);
    public Orders updateOrder(Long id, Orders orders);
    public void deleteOrder(Long id);
    public Orders addOrderRow(Long id, OrderRow orderRow);
}

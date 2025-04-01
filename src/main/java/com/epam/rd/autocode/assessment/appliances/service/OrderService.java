package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.dto.OrdersDTO;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface OrderService {
    Page<OrdersDTO> getAllOrders(Pageable pageable);
    Page<OrdersDTO> searchOrders(String search, Pageable pageable);
    Page<OrdersDTO> getOrdersByClientId(Long clientId, Pageable pageable);
    Page<OrdersDTO> searchOrdersForUser(Long clientId, String search, Pageable pageable);

    Orders createOrder(Orders orders);
    OrdersDTO getOrderById(Long id);
    OrdersDTO updateOrder(Long id, OrdersDTO ordersDto);
    void deleteOrder(Long id);

    OrdersDTO approvedOrder(Long id, boolean approved);
    OrdersDTO addApplianceToOrder(Long ordersId, Long applianceId, Long numbers, BigDecimal price);
}

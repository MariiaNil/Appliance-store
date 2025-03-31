package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.dto.OrdersDTO;
import com.epam.rd.autocode.assessment.appliances.mapper.OrdersDTOMapper;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.OrderRow;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.repository.ApplianceRepository;
import com.epam.rd.autocode.assessment.appliances.repository.OrdersRepository;
import com.epam.rd.autocode.assessment.appliances.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrdersRepository ordersRepository;
    private final OrdersDTOMapper ordersDTOMapper;
    private final ApplianceRepository applianceRepository;

    @Override
    public Page<OrdersDTO> getAllOrders(Pageable pageable) {
        Page<Orders> ordersPage = ordersRepository.findAll(pageable);
        return ordersPage.map(ordersDTOMapper);
    }

    @Override
    public Page<OrdersDTO> searchOrders(String search, Pageable pageable) {
        return ordersRepository.findByClient_NameContainingIgnoreCase(search, pageable)
                .map(ordersDTOMapper);
    }

    @Override
    @Transactional
    public Orders createOrder(Orders orders) {
        return ordersRepository.save(orders);
    }

    @Override
    public OrdersDTO getOrderById(Long id) {
        Orders orders = ordersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return ordersDTOMapper.apply(orders);
    }

    @Override
    public OrdersDTO updateOrder(Long id, OrdersDTO ordersDto) {
        return null;
    }

    @Override
    public void deleteOrder(Long id) {
        ordersRepository.deleteById(id);
    }

    @Override
    public OrdersDTO approvedOrder(Long id, boolean approved) {
        Orders orders = ordersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        orders.setApproved(approved);
        Orders updatedOrder = ordersRepository.save(orders);
        return ordersDTOMapper.apply(updatedOrder);
    }

    @Override
    public OrdersDTO addApplianceToOrder(Long ordersId, Long applianceId, Long numbers, BigDecimal price) {
        Orders orders = ordersRepository.findById(ordersId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        Appliance appliance = applianceRepository.findById(applianceId)
                .orElseThrow(() -> new RuntimeException("Appliance not found"));
        OrderRow orderRow = new OrderRow();
        orderRow.setAppliance(appliance);
        orderRow.setNumber(numbers);
        orderRow.setAmount(price.multiply(BigDecimal.valueOf(numbers)));
        orders.getOrderRowSet().add(orderRow);
        Orders updatedOrder = ordersRepository.save(orders);
        return ordersDTOMapper.apply(updatedOrder);
    }
}

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('EMPLOYEE')")
    public Page<OrdersDTO> getAllOrders(Pageable pageable) {
        Page<Orders> ordersPage = ordersRepository.findAll(pageable);
        return ordersPage.map(ordersDTOMapper);
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYEE')")
    public Page<OrdersDTO> searchOrders(String search, Pageable pageable) {
        return ordersRepository.findByClient_NameContainingIgnoreCase(search, pageable)
                .map(ordersDTOMapper);
    }

    @Override
    @PreAuthorize("#clientId == principal.id")
    public Page<OrdersDTO> getOrdersByClientId(Long clientId, Pageable pageable) {
        Page<Orders> ordersPage = ordersRepository.findAllByClient_Id(clientId, pageable);
        return ordersPage.map(ordersDTOMapper);
    }

    @Override
    public Page<OrdersDTO> searchOrdersForUser(Long clientId, String search, Pageable pageable) {
        return ordersRepository.findAllByClient_Id(clientId, search, pageable)
                .map(ordersDTOMapper);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('EMPLOYEE')")
    public Orders createOrder(Orders orders) {
        return ordersRepository.save(orders);
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYEE')")
    public OrdersDTO getOrderById(Long id) {
        Orders orders = ordersRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Order not found"));
        return ordersDTOMapper.apply(orders);
    }

    @Override
    public OrdersDTO updateOrder(Long id, OrdersDTO ordersDto) {
        return null;
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYEE')")
    public void deleteOrder(Long id) {
        ordersRepository.deleteById(id);
    }

    @Override
    @PreAuthorize("hasRole('EMPLOYEE')")
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
                .orElseThrow(() ->
                        new RuntimeException("Appliance not found"));
        OrderRow orderRow = new OrderRow();
        orderRow.setAppliance(appliance);
        orderRow.setNumber(numbers);
        orderRow.setAmount(price.multiply(BigDecimal.valueOf(numbers)));
        orders.getOrderRowSet().add(orderRow);
        Orders updatedOrder = ordersRepository.save(orders);
        return ordersDTOMapper.apply(updatedOrder);
    }
}

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrdersRepository ordersRepository;
    private final OrdersDTOMapper ordersDTOMapper;
    private final ApplianceRepository applianceRepository;

    @Override
    public Page<OrdersDTO> getAllOrders(Pageable pageable) {
        Page<Orders> ordersPage = ordersRepository.findAll(pageable);
        logger.info("Orders found: {}", ordersPage.getTotalElements());
        return ordersPage.map(ordersDTOMapper);
    }

    @Override
    public Page<OrdersDTO> searchOrders(String search, Pageable pageable) {
        logger.info("Searching orders by client name: {}", search);
        return ordersRepository.findByClient_NameContainingIgnoreCase(search, pageable)
                .map(ordersDTOMapper);
    }

    @Override
    @Transactional
    public Orders createOrder(Orders orders) {
        logger.info("Creating order: {}", orders);
        return ordersRepository.save(orders);
    }

    @Override
    public OrdersDTO getOrderById(Long id) {
        logger.info("Getting order by ID: {}", id);
        Orders orders = ordersRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Order with ID {} not found", id);
                    return new RuntimeException("Order not found");
                });
        return ordersDTOMapper.apply(orders);
    }

    @Override
    public OrdersDTO updateOrder(Long id, OrdersDTO ordersDto) {
        return null;
    }

    @Override
    public void deleteOrder(Long id) {
        ordersRepository.deleteById(id);
        logger.info("Order with ID {} deleted", id);
    }

    @Override
    public OrdersDTO approvedOrder(Long id, boolean approved) {
        logger.info("Approving order with ID: {}", id);
        Orders orders = ordersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        orders.setApproved(approved);
        Orders updatedOrder = ordersRepository.save(orders);
        return ordersDTOMapper.apply(updatedOrder);
    }

    @Override
    public OrdersDTO addApplianceToOrder(Long ordersId, Long applianceId, Long numbers, BigDecimal price) {
        logger.info("Adding appliance with ID {} to order with ID {}", applianceId, ordersId);
        Orders orders = ordersRepository.findById(ordersId)
                .orElseThrow(() ->  {
                    logger.error("Order with ID {} not found", ordersId);
                    return new RuntimeException("Order not found");
                });
        Appliance appliance = applianceRepository.findById(applianceId)
                .orElseThrow(() -> {
                    logger.error("Appliance with ID {} not found", applianceId);
                    return new RuntimeException("Appliance not found");
                });
        OrderRow orderRow = new OrderRow();
        orderRow.setAppliance(appliance);
        orderRow.setNumber(numbers);
        orderRow.setAmount(price.multiply(BigDecimal.valueOf(numbers)));
        orders.getOrderRowSet().add(orderRow);
        Orders updatedOrder = ordersRepository.save(orders);
        logger.info("Appliance with ID {} added to order with ID {}", applianceId, ordersId);
        return ordersDTOMapper.apply(updatedOrder);
    }
}

package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.dto.OrdersDTO;
import com.epam.rd.autocode.assessment.appliances.mapper.OrdersDTOMapper;
import com.epam.rd.autocode.assessment.appliances.model.OrderRow;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.repository.OrderRowRepository;
import com.epam.rd.autocode.assessment.appliances.repository.OrdersRepository;
import com.epam.rd.autocode.assessment.appliances.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrdersRepository ordersRepository;
    private final OrderRowRepository orderRowRepository;
    private final OrdersDTOMapper ordersDTOMapper;

    @Override
    public List<OrdersDTO> getAllOrders() {
        return ordersRepository.findAll()
                .stream()
                .map(ordersDTOMapper)
                .toList();
    }

    @Override
    public Orders createOrder(Orders orders) {
        return ordersRepository.save(orders);
    }

    @Override
    public Optional<OrdersDTO> getOrderById(Long id) {
        return Optional.ofNullable(ordersRepository.findById(id)
                        .map(ordersDTOMapper)
                .orElseThrow(() -> new RuntimeException("Order not found")));
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
    public Orders addOrderRow(Long id, OrderRow orderRow) {
        Optional<Orders> ordersOptional = ordersRepository.findById(id);
        if (ordersOptional.isPresent()) {
            Orders orders = ordersOptional.get();
            orders.getOrderRowSet().add(orderRow);
            return ordersRepository.save(orders);
        }
        throw new IllegalArgumentException("Order not found with id: " + id);
    }
}

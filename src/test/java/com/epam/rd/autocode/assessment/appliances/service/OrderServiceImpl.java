package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.dto.*;
import com.epam.rd.autocode.assessment.appliances.exception.ApplianceNotFoundException;
import com.epam.rd.autocode.assessment.appliances.exception.OrderNotFoundException;
import com.epam.rd.autocode.assessment.appliances.mapper.OrdersDTOMapper;
import com.epam.rd.autocode.assessment.appliances.model.*;
import com.epam.rd.autocode.assessment.appliances.repository.ApplianceRepository;
import com.epam.rd.autocode.assessment.appliances.repository.EmployeeRepository;
import com.epam.rd.autocode.assessment.appliances.repository.OrdersRepository;
import com.epam.rd.autocode.assessment.appliances.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @Mock
    private OrdersRepository ordersRepository;
    @Mock
    private OrdersDTOMapper ordersDTOMapper;
    @Mock
    private ApplianceRepository applianceRepository;
    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private OrderServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void testGetAllOrders() {
        Pageable pageable = PageRequest.of(0, 5);
        Orders order = new Orders();
        OrdersDTO dto = new OrdersDTO( 1L,
                new ClientDTO(1L, "John", "Doe", "john.doe@example.com", "1234567890"),
                new EmployeeDTO(2L, "Alice", "Smith", "Manager", "HR"),
                Set.of(
                        new OrderRowDTO(1L, new ApplianceDTO(1L,
                                "TestName",
                                Category.SMALL,
                                "TestModel",
                                null,
                                PowerType.AC110,
                                "-----",
                                "TestDesc",
                                Integer.valueOf(77),
                                BigDecimal.valueOf(100)), 2L, new BigDecimal("19.99")),
                        new OrderRowDTO(2L, new ApplianceDTO(1L,
                                "TestName",
                                Category.SMALL,
                                "TestModel",
                                null,
                                PowerType.AC110,
                                "-----",
                                "TestDesc",
                                Integer.valueOf(77),
                                BigDecimal.valueOf(100)), 1L, new BigDecimal("9.99"))
                ),
                true,
                new BigDecimal("49.97")
        );
        Page<Orders> page = new PageImpl<>(Collections.singletonList(order));

        when(ordersRepository.findAll(pageable)).thenReturn(page);
        when(ordersDTOMapper.apply(order)).thenReturn(dto);

        Page<OrdersDTO> result = service.getAllOrders(pageable);
        assertEquals(1, result.getContent().size());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void testSearchOrders() {
        String search = "clientName";
        Pageable pageable = PageRequest.of(0, 5);
        Orders order = new Orders();
        OrdersDTO dto = new OrdersDTO( 1L,
                new ClientDTO(1L, "John", "Doe", "john.doe@example.com", "1234567890"),
                new EmployeeDTO(2L, "Alice", "Smith", "Manager", "HR"),
                Set.of(
                        new OrderRowDTO(1L, new ApplianceDTO(1L,
                                "TestName",
                                Category.SMALL,
                                "TestModel",
                                null,
                                PowerType.AC110,
                                "-----",
                                "TestDesc",
                                Integer.valueOf(77),
                                BigDecimal.valueOf(100)), 2L, new BigDecimal("19.99")),
                        new OrderRowDTO(2L, new ApplianceDTO(1L,
                                "TestName",
                                Category.SMALL,
                                "TestModel",
                                null,
                                PowerType.AC110,
                                "-----",
                                "TestDesc",
                                Integer.valueOf(77),
                                BigDecimal.valueOf(100)), 1L, new BigDecimal("9.99"))
                ),
                true,
                new BigDecimal("49.97")
        );
        Page<Orders> page = new PageImpl<>(Collections.singletonList(order));

        when(ordersRepository.findByClient_NameContainingIgnoreCase(search, pageable)).thenReturn(page);
        when(ordersDTOMapper.apply(order)).thenReturn(dto);

        Page<OrdersDTO> result = service.searchOrders(search, pageable);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testGetOrdersByClientId() {
        Long clientId = 1L;
        Pageable pageable = PageRequest.of(0, 5);
        Orders order = new Orders();
        OrdersDTO dto = new OrdersDTO( 1L,
                new ClientDTO(1L, "John", "Doe", "john.doe@example.com", "1234567890"),
                new EmployeeDTO(2L, "Alice", "Smith", "Manager", "HR"),
                Set.of(
                        new OrderRowDTO(1L, new ApplianceDTO(1L,
                                "TestName",
                                Category.SMALL,
                                "TestModel",
                                null,
                                PowerType.AC110,
                                "-----",
                                "TestDesc",
                                Integer.valueOf(77),
                                BigDecimal.valueOf(100)), 2L, new BigDecimal("19.99")),
                        new OrderRowDTO(2L, new ApplianceDTO(1L,
                                "TestName",
                                Category.SMALL,
                                "TestModel",
                                null,
                                PowerType.AC110,
                                "-----",
                                "TestDesc",
                                Integer.valueOf(77),
                                BigDecimal.valueOf(100)), 1L, new BigDecimal("9.99"))
                ),
                true,
                new BigDecimal("49.97")
        );
        Page<Orders> page = new PageImpl<>(Collections.singletonList(order));

        when(ordersRepository.findAllByClient_Id(clientId, pageable)).thenReturn(page);
        when(ordersDTOMapper.apply(order)).thenReturn(dto);

        Page<OrdersDTO> result = service.getOrdersByClientId(clientId, pageable);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testUpdateEmployeeForOrder_SetEmployee() {
        Long orderId = 1L;
        Long employeeId = 2L;
        Orders order = new Orders();
        Employee employee = new Employee();
        employee.setId(employeeId);

        when(ordersRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(ordersRepository.save(order)).thenReturn(order);

        service.updateEmployeeForOrder(orderId, employeeId);
        assertEquals(employee, order.getEmployee());
    }

    @Test
    void testUpdateEmployeeForOrder_NullEmployee() {
        Long orderId = 1L;
        Orders order = new Orders();
        order.setEmployee(new Employee());
        when(ordersRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(ordersRepository.save(order)).thenReturn(order);

        service.updateEmployeeForOrder(orderId, null);
        assertNull(order.getEmployee());
    }

    @Test
    @WithMockUser(roles = {"EMPLOYEE", "CLIENT"})
    void testCreateOrder() {
        Orders order = new Orders();
        when(ordersRepository.save(order)).thenReturn(order);
        Orders created = service.createOrder(order);
        assertEquals(order, created);
    }

    @Test
    void testGetOrderByIdFound() {
        Long orderId = 1L;
        Orders order = new Orders();
        OrdersDTO dto = new OrdersDTO( 1L,
                new ClientDTO(1L, "John", "Doe", "john.doe@example.com", "1234567890"),
                new EmployeeDTO(2L, "Alice", "Smith", "Manager", "HR"),
                Set.of(
                        new OrderRowDTO(1L, new ApplianceDTO(1L,
                                "TestName",
                                Category.SMALL,
                                "TestModel",
                                null,
                                PowerType.AC110,
                                "-----",
                                "TestDesc",
                                Integer.valueOf(77),
                                BigDecimal.valueOf(100)), 2L, new BigDecimal("19.99")),
                        new OrderRowDTO(2L, new ApplianceDTO(1L,
                                "TestName",
                                Category.SMALL,
                                "TestModel",
                                null,
                                PowerType.AC110,
                                "-----",
                                "TestDesc",
                                Integer.valueOf(77),
                                BigDecimal.valueOf(100)), 1L, new BigDecimal("9.99"))
                ),
                true,
                new BigDecimal("49.97")
        );
        when(ordersRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(ordersDTOMapper.apply(order)).thenReturn(dto);

        OrdersDTO result = service.getOrderById(orderId);
        assertEquals(dto, result);
    }

    @Test
    void testGetOrderByIdNotFound() {
        Long orderId = 1L;
        when(ordersRepository.findById(orderId)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> service.getOrderById(orderId));
    }

    @Test
    void testDeleteOrder() {
        Long orderId = 1L;
        service.deleteOrder(orderId);
        verify(ordersRepository).deleteById(orderId);
    }

    @Test
    void testApprovedOrder() {
        Long orderId = 1L;
        Orders order = new Orders();
        OrdersDTO dto = new OrdersDTO( 1L,
                new ClientDTO(1L, "John", "Doe", "john.doe@example.com", "1234567890"),
                new EmployeeDTO(2L, "Alice", "Smith", "Manager", "HR"),
                Set.of(
                        new OrderRowDTO(1L, new ApplianceDTO(1L,
                                "TestName",
                                Category.SMALL,
                                "TestModel",
                                null,
                                PowerType.AC110,
                                "-----",
                                "TestDesc",
                                Integer.valueOf(77),
                                BigDecimal.valueOf(100)), 2L, new BigDecimal("19.99")),
                        new OrderRowDTO(2L, new ApplianceDTO(1L,
                                "TestName",
                                Category.SMALL,
                                "TestModel",
                                null,
                                PowerType.AC110,
                                "-----",
                                "TestDesc",
                                Integer.valueOf(77),
                                BigDecimal.valueOf(100)), 1L, new BigDecimal("9.99"))
                ),
                true,
                new BigDecimal("49.97")
        );

        when(ordersRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(ordersRepository.save(order)).thenReturn(order);
        when(ordersDTOMapper.apply(order)).thenReturn(dto);

        OrdersDTO result = service.approvedOrder(orderId, true);
        assertEquals(dto, result);
        assertTrue(order.getApproved());
    }

    @Test
    void testAddApplianceToOrder() {
        Long orderId = 1L;
        Long applianceId = 2L;
        Long numbers = 3L;
        BigDecimal price = BigDecimal.valueOf(10);
        Orders order = new Orders();
        order.setOrderRowSet(new java.util.HashSet<>());
        Appliance appliance = new Appliance();
        OrdersDTO dto = new OrdersDTO( 1L,
                new ClientDTO(1L, "John", "Doe", "john.doe@example.com", "1234567890"),
                new EmployeeDTO(2L, "Alice", "Smith", "Manager", "HR"),
                Set.of(
                        new OrderRowDTO(1L, new ApplianceDTO(1L,
                                "TestName",
                                Category.SMALL,
                                "TestModel",
                                null,
                                PowerType.AC110,
                                "-----",
                                "TestDesc",
                                Integer.valueOf(77),
                                BigDecimal.valueOf(100)), 2L, new BigDecimal("19.99")),
                        new OrderRowDTO(2L, new ApplianceDTO(1L,
                                "TestName",
                                Category.SMALL,
                                "TestModel",
                                null,
                                PowerType.AC110,
                                "-----",
                                "TestDesc",
                                Integer.valueOf(77),
                                BigDecimal.valueOf(100)), 1L, new BigDecimal("9.99"))
                ),
                true,
                new BigDecimal("49.97")
        );

        when(ordersRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(applianceRepository.findById(applianceId)).thenReturn(Optional.of(appliance));
        when(ordersRepository.save(order)).thenReturn(order);
        when(ordersDTOMapper.apply(order)).thenReturn(dto);

        OrdersDTO result = service.addApplianceToOrder(orderId, applianceId, numbers, price);
        assertFalse(order.getOrderRowSet().isEmpty());
        verify(ordersRepository).save(order);
        assertEquals(dto, result);
    }

    @Test
    void testAddApplianceToOrder_ApplianceNotFound() {
        Long orderId = 1L;
        Long applianceId = 2L;
        when(ordersRepository.findById(orderId)).thenReturn(Optional.of(new Orders()));
        when(applianceRepository.findById(applianceId)).thenReturn(Optional.empty());
        assertThrows(ApplianceNotFoundException.class,
                () -> service.addApplianceToOrder(orderId, applianceId, 1L, BigDecimal.TEN));
    }
}


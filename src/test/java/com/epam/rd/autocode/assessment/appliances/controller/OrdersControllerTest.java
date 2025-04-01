package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.dto.ClientDTO;
import com.epam.rd.autocode.assessment.appliances.dto.EmployeeDTO;
import com.epam.rd.autocode.assessment.appliances.dto.OrdersDTO;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import com.epam.rd.autocode.assessment.appliances.service.EmployeeService;
import com.epam.rd.autocode.assessment.appliances.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdersControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private ClientService clientService;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private ApplianceService applianceService;

    @Mock
    private Model model;

    @Mock
    private Pageable pageable;

    @Mock
    private Page<OrdersDTO> ordersPage;

    @InjectMocks
    private OrdersController controller;

    @Test
    void testListOrdersWithoutSearch() {
        when(orderService.getAllOrders(any(Pageable.class))).thenReturn(ordersPage);

        String view = controller.listOrders(model, null, pageable);

        verify(orderService).getAllOrders(pageable);
        verify(model).addAttribute("ordersPage", ordersPage);
        verify(model).addAttribute("search", null);
        assertEquals("order/orders", view);
    }

    @Test
    void testListOrdersWithSearch() {
        String search = "order";
        when(orderService.searchOrders(eq(search), any(Pageable.class))).thenReturn(ordersPage);

        String view = controller.listOrders(model, search, pageable);

        verify(orderService).searchOrders(eq(search), eq(pageable));
        verify(model).addAttribute("ordersPage", ordersPage);
        verify(model).addAttribute("search", search);
        assertEquals("order/orders", view);
    }

    @Test
    void testShowEditOrders() {
        Long orderId = 1L;
        OrdersDTO ordersDTO = mock(OrdersDTO.class);
        when(orderService.getOrderById(orderId)).thenReturn(ordersDTO);
        // Создаём тестовую страницу с пустым списком
        Pageable pageableLocal = PageRequest.of(0, 20, Sort.by("id").ascending());
        when(clientService.getClients(any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));
        when(employeeService.getEmployees(any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));

        String view = controller.showEditOrders(orderId, model, 20);

        verify(orderService).getOrderById(orderId);
        verify(model).addAttribute("order", ordersDTO);
        verify(model).addAttribute(eq("clients"), any());
        verify(model).addAttribute(eq("employees"), any());
        assertEquals("order/editOrder", view);
    }

    @Test
    void testShowChoiceAppliance() {
        Long orderId = 1L;
        OrdersDTO ordersDTO = mock(OrdersDTO.class);
        when(orderService.getOrderById(orderId)).thenReturn(ordersDTO);
        when(applianceService.getAllAppliancesList()).thenReturn(List.of());

        String view = controller.showChoiceAppliance(orderId, model);

        verify(orderService).getOrderById(orderId);
        verify(model).addAttribute("appliances", List.of());
        verify(model).addAttribute("ordersId", ordersDTO.id());
        assertEquals("order/choiceAppliance", view);
    }

    @Test
    void testDeleteOrder() {
        Long orderId = 1L;
        String view = controller.deleteOrder(orderId);

        verify(orderService).deleteOrder(orderId);
        assertEquals("redirect:/orders", view);
    }

    @Test
    void testShowAddOrders() {
        when(clientService.getAllClientsList()).thenReturn(List.of());
        when(employeeService.getAllEmployeesList()).thenReturn(List.of());

        String view = controller.showAddOrders(model);

        verify(model).addAttribute("clients", List.of());
        verify(model).addAttribute("employees", List.of());
        verify(model).addAttribute(eq("order"), any(Orders.class));
        assertEquals("order/newOrder", view);
    }

    @Test
    void testCreateOrder() {
        Orders orders = new Orders();
        String view = controller.createOrder(orders);

        verify(orderService).createOrder(orders);
        assertEquals("redirect:/orders", view);
    }

    @Test
    void testApprovedOrder() {
        Long orderId = 1L;
        String view = controller.approvedOrder(orderId);

        verify(orderService).approvedOrder(orderId, true);
        assertEquals("redirect:/orders", view);
    }

    @Test
    void testUnapprovedOrder() {
        Long orderId = 1L;
        String view = controller.unapprovedOrder(orderId);

        verify(orderService).approvedOrder(orderId, false);
        assertEquals("redirect:/orders", view);
    }

    @Test
    void testAddIntoOrder() {
        Long ordersId = 1L;
        Long applianceId = 2L;
        Long numbers = 3L;
        BigDecimal price = BigDecimal.valueOf(100);

        String view = controller.addIntoOrder(ordersId, applianceId, numbers, price);

        verify(orderService).addApplianceToOrder(ordersId, applianceId, numbers, price);
        assertEquals("redirect:/orders", view);
    }
}

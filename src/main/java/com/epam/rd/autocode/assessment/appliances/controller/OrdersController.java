package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.dto.ApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.dto.ClientDTO;
import com.epam.rd.autocode.assessment.appliances.dto.EmployeeDTO;
import com.epam.rd.autocode.assessment.appliances.dto.OrdersDTO;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.service.ApplianceService;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import com.epam.rd.autocode.assessment.appliances.service.EmployeeService;
import com.epam.rd.autocode.assessment.appliances.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrdersController {

    private static final Logger logger = LoggerFactory.getLogger(OrdersController.class);
    private final OrderService orderService;
    private final ClientService clientService;
    private final EmployeeService employeeService;
    private final ApplianceService applianceService;


    @GetMapping
    public String listOrders(
            Model model,
            @RequestParam(value = "search", required = false) String search,
            @PageableDefault(size = 5, sort = {"id", "client", "employee"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<OrdersDTO> ordersPage;
        if (search != null && !search.trim().isEmpty())
            ordersPage = orderService.searchOrders(search, pageable);
        else
            ordersPage = orderService.getAllOrders(pageable);
        model.addAttribute("ordersPage", ordersPage);
        model.addAttribute("search", search);
        logger.info("Orders list successfully loaded, returning view 'order/orders'");
        return "order/orders";
    }

    @GetMapping("/{id}/edit")
    public String showEditOrders(@PathVariable("id") Long id,
                                 Model model,
                                 @RequestParam(value = "size", defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(0, size, Sort.by("id").ascending());
        List<ClientDTO> clientDTOS = clientService.getClients(pageable).getContent();
        List<EmployeeDTO> employeeDTOS = employeeService.getEmployees(pageable).getContent();

        OrdersDTO orders = orderService.getOrderById(id);
        model.addAttribute("order", orders);
        model.addAttribute("clients", clientDTOS);
        model.addAttribute("employees", employeeDTOS);
        logger.info("Showing edit order form");
        return "order/editOrder";
    }

    @GetMapping("/{id}/choice-appliance")
    public String showChoiceAppliance(@PathVariable("id") Long id, Model model) {
        OrdersDTO orders = orderService.getOrderById(id);
        model.addAttribute("appliances", applianceService.getAllAppliancesList());
        model.addAttribute("ordersId", orders.id());
        logger.info("Showing choice appliance form");
        return "order/choiceAppliance";
    }

    @GetMapping("/{id}/delete")
    public String deleteOrder(@PathVariable("id") Long id) {
        orderService.deleteOrder(id);
        logger.info("Order deleted successfully");
        return "redirect:/orders";
    }

    @GetMapping("/add")
    public String showAddOrders(Model model) {
        model.addAttribute("clients", clientService.getAllClientsList());
        model.addAttribute("employees", employeeService.getAllEmployeesList());
        model.addAttribute("order", new Orders());
        logger.info("Showing add order form");
        return "order/newOrder";
    }

    @PostMapping("/add-order")
    public String createOrder(Orders orders) {
        orderService.createOrder(orders);
        logger.info("Order created successfully");
        return "redirect:/orders";
    }

    @GetMapping("/{id}/approved")
    public String approvedOrder(@PathVariable("id") Long id) {
        orderService.approvedOrder(id, true);
        logger.info("Order approved successfully");
        return "redirect:/orders";
    }

    @GetMapping("/{id}/unapproved")
    public String unapprovedOrder(@PathVariable("id") Long id) {
        orderService.approvedOrder(id, false);
        logger.info("Order unapproved successfully");
        return "redirect:/orders";
    }

    @PostMapping("/{ordersId}/add-into-order")
    public String addIntoOrder(@RequestParam("ordersId") Long ordersId,
                               @RequestParam("applianceId") Long applianceId,
                               @RequestParam("numbers") Long numbers,
                               @RequestParam("price") BigDecimal price) {
        orderService.addApplianceToOrder(ordersId, applianceId, numbers, price);
        logger.info("Appliance added to order successfully");
        return "redirect:/orders";
    }
}

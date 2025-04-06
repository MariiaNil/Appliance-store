package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.dto.ApplianceDTO;
import com.epam.rd.autocode.assessment.appliances.dto.ClientDTO;
import com.epam.rd.autocode.assessment.appliances.dto.EmployeeDTO;
import com.epam.rd.autocode.assessment.appliances.dto.OrdersDTO;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.service.*;
import com.epam.rd.autocode.assessment.appliances.service.impl.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrdersController {

    private final OrderService orderService;
    private final ClientService clientService;
    private final EmployeeService employeeService;
    private final ApplianceService applianceService;


    @GetMapping("/my-orders")
    public String clientListOrders(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                   Model model,
                                   @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<OrdersDTO> ordersPage = orderService.getOrdersByClientId(userDetails.getUserId(), pageable);
        model.addAttribute("ordersPage", ordersPage);
        return "order/my-orders";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam("applianceId") Long applianceId,
                            @RequestParam("quantity") Long quantity,
                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Optional<OrdersDTO> cartOrderOpt = orderService.getOrdersByClientId(userDetails.getUserId(), Pageable.unpaged())
                .getContent().stream()
                .filter(order -> Boolean.FALSE.equals(order.approved()))
                .findFirst();

        OrdersDTO cartOrder;
        if (cartOrderOpt.isPresent()) {
            cartOrder = cartOrderOpt.get();
        } else {
            Client client = clientService.getClientEntityById(userDetails.getUserId());
            Orders newOrder = new Orders();
            newOrder.setClient(client);
            newOrder.setApproved(false);
            Orders createdOrder = orderService.createOrder(newOrder);
            cartOrder = orderService.getOrderById(createdOrder.getId());
        }
        ApplianceDTO applianceDto = applianceService.getApplianeById(applianceId);
        BigDecimal appliancePrice = applianceDto.price();
        orderService.addApplianceToOrder(cartOrder.id(), applianceId, quantity, appliancePrice);
        return "redirect:/orders/my-orders";
    }

    @PostMapping("/updateEmployee")
    public String updateEmployeeForOrder(@RequestParam("orderId") Long orderId,
                                         @RequestParam(value = "employeeId", required = false) Long employeeId) {
        orderService.updateEmployeeForOrder(orderId, employeeId);
        return "redirect:/orders";
    }



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
        return "order/editOrder";
    }

    @GetMapping("/{id}/choice-appliance")
    public String showChoiceAppliance(@PathVariable("id") Long id, Model model) {
        OrdersDTO orders = orderService.getOrderById(id);
        model.addAttribute("appliances", applianceService.getAllAppliancesList());
        model.addAttribute("ordersId", orders.id());
        return "order/choiceAppliance";
    }

    @GetMapping("/{id}/delete")
    public String deleteOrder(@PathVariable("id") Long id) {
        orderService.deleteOrder(id);
        return "redirect:/orders";
    }

    @GetMapping("/my-orders/{id}/delete")
    public String deleteMyOrder(@PathVariable("id") Long id) {
        orderService.deleteOrder(id);
        return "redirect:/orders/my-orders";
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/add")
    public String showAddOrders(Model model) {
        model.addAttribute("clients", clientService.getAllClientsList());
        model.addAttribute("employees", employeeService.getAllEmployeesList());
        model.addAttribute("order", new Orders());
        return "order/newOrder";
    }

    @PostMapping("/add-order")
    public String createOrder(Orders orders) {
        orderService.createOrder(orders);
        return "redirect:/orders";
    }

    @GetMapping("/{id}/approved")
    public String approvedOrder(@PathVariable("id") Long id) {
        orderService.approvedOrder(id, true);
        return "redirect:/orders";
    }

    @GetMapping("/{id}/unapproved")
    public String unapprovedOrder(@PathVariable("id") Long id) {
        orderService.approvedOrder(id, false);
        return "redirect:/orders";
    }

    @PostMapping("/{ordersId}/add-into-order")
    public String addIntoOrder(@RequestParam("ordersId") Long ordersId,
                               @RequestParam("applianceId") Long applianceId,
                               @RequestParam("numbers") Long numbers,
                               @RequestParam("price") BigDecimal price) {
        orderService.addApplianceToOrder(ordersId, applianceId, numbers, price);
        return "redirect:/orders";
    }
}

package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.dto.ClientDTO;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public String listClients(
            Model model,
            @RequestParam(value = "search", required = false) String search,
            @PageableDefault(size = 5, sort = {"id", "name", "email", "card"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ClientDTO> clientsPage;
        if (search != null && !search.trim().isEmpty())
            clientsPage = clientService.searchClients(search, pageable);
        else
            clientsPage = clientService.getClients(pageable);
        model.addAttribute("clientsPage", clientsPage);
        model.addAttribute("search", search);
        return "client/clients";
    }

    @GetMapping("/add")
    public String showAddClientForm(Model model) {
        model.addAttribute("client", new Client());
        return "client/newClient";
    }

    @PostMapping("/add-client")
    public String addClient(Client client) {
        clientService.createClient(client);
        return "redirect:/clients";
    }

    @GetMapping("/{id}/delete")
    public String deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return "redirect:/clients";
    }
}

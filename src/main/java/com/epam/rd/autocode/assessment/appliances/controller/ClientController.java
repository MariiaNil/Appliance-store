package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.dto.ClientDTO;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public String listClients(Model model) {
        List<ClientDTO> clientList = clientService.getClients();
        model.addAttribute("clients", clientList);
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

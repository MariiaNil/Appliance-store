package com.epam.rd.autocode.assessment.appliances.controller;

import com.epam.rd.autocode.assessment.appliances.dto.ClientDTO;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @Mock
    private Model model;

    @Mock
    private Pageable pageable;

    @Mock
    private Page<ClientDTO> clientPage;

    @InjectMocks
    private ClientController controller;

    @Test
    void testListClientsWithoutSearch() {
        when(clientService.getClients(any(Pageable.class))).thenReturn(clientPage);

        String view = controller.listClients(model, null, pageable);

        verify(clientService).getClients(pageable);
        verify(model).addAttribute("clientsPage", clientPage);
        verify(model).addAttribute("search", null);
        assertEquals("client/clients", view);
    }

    @Test
    void testListClientsWithSearch() {
        String search = "John";
        when(clientService.searchClients(eq(search), any(Pageable.class))).thenReturn(clientPage);

        String view = controller.listClients(model, search, pageable);

        verify(clientService).searchClients(eq(search), eq(pageable));
        verify(model).addAttribute("clientsPage", clientPage);
        verify(model).addAttribute("search", search);
        assertEquals("client/clients", view);
    }

    @Test
    void testShowAddClientForm() {
        String view = controller.showAddClientForm(model);

        verify(model).addAttribute(eq("client"), any(Client.class));
        assertEquals("client/newClient", view);
    }

    @Test
    void testAddClient() {
        Client client = new Client();
        String view = controller.addClient(client);

        verify(clientService).createClient(client);
        assertEquals("redirect:/clients", view);
    }

    @Test
    void testDeleteClient() {
        Long id = 1L;
        String view = controller.deleteClient(id);

        verify(clientService).deleteClient(id);
        assertEquals("redirect:/clients", view);
    }
}

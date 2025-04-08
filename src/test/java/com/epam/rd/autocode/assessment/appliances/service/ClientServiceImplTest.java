package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.dto.ClientDTO;
import com.epam.rd.autocode.assessment.appliances.exception.ClientNotFoundException;
import com.epam.rd.autocode.assessment.appliances.mapper.ClientDTOMapper;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.repository.ClientRepository;
import com.epam.rd.autocode.assessment.appliances.service.impl.ClientServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private EntityManager entityManager;
    @Mock
    private ClientDTOMapper clientDTOMapper;
    @Mock
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @InjectMocks
    private ClientServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateClient() {
        Client client = new Client();
        client.setPassword("rawPassword");
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(clientRepository.save(client)).thenReturn(client);

        Client created = service.createClient(client);
        assertEquals("encodedPassword", client.getPassword());
        verify(passwordEncoder).encode("rawPassword");
        verify(clientRepository).save(client);
    }

    @Test
    void testGetClientByIdFound() {
        Long id = 1L;
        Client client = new Client();
        ClientDTO dto = new ClientDTO(1L, "TestClient", "test@example.com", "1234567890", "2223-3333");
        when(clientRepository.findById(id)).thenReturn(Optional.of(client));
        when(clientDTOMapper.apply(client)).thenReturn(dto);

        ClientDTO result = service.getClientById(id);
        assertEquals(dto, result);
    }

    @Test
    void testGetClientByIdNotFound() {
        Long id = 1L;
        when(clientRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ClientNotFoundException.class, () -> service.getClientById(id));
    }

    @Test
    void testGetClientEntityByIdFound() {
        Long id = 1L;
        Client client = new Client();
        when(clientRepository.findById(id)).thenReturn(Optional.of(client));

        Client result = service.getClientEntityById(id);
        assertEquals(client, result);
    }

    @Test
    void testGetClients() {
        Pageable pageable = PageRequest.of(0, 5);
        Client client = new Client();
        ClientDTO dto = new ClientDTO(1L, "TestClient", "test@example.com", "1234567890", "2223-3333");
        Page<Client> clientPage = new PageImpl<>(Collections.singletonList(client));

        when(clientRepository.findAll(pageable)).thenReturn(clientPage);
        when(clientDTOMapper.apply(client)).thenReturn(dto);

        Page<ClientDTO> result = service.getClients(pageable);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testDeleteClient() {
        Long id = 1L;
        Client client = new Client();
        when(entityManager.find(Client.class, id)).thenReturn(client);

        TypedQuery<Orders> query = mock(TypedQuery.class);
        when(entityManager.createQuery("SELECT o FROM Orders o WHERE o.client = :client", Orders.class)).thenReturn(query);
        when(query.setParameter("client", client)).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.singletonList(new Orders()));

        service.deleteClient(id);
        verify(entityManager).remove(client);
    }

    @Test
    void testGetAllClientsList() {
        Client client = new Client();
        ClientDTO dto = new ClientDTO(1L, "TestClient", "test@example.com", "1234567890", "2223-3333");
        when(clientRepository.findAll()).thenReturn(Collections.singletonList(client));
        when(clientDTOMapper.apply(client)).thenReturn(dto);

        List<ClientDTO> list = service.getAllClientsList();
        assertEquals(1, list.size());
    }

    @Test
    void testSearchClients() {
        String search = "John";
        Pageable pageable = PageRequest.of(0, 5);
        Client client = new Client();
        ClientDTO dto = new ClientDTO(1L, "TestClient", "test@example.com", "1234567890", "2223-3333");
        Page<Client> page = new PageImpl<>(Collections.singletonList(client));

        when(clientRepository.findByNameContainingIgnoreCase(search, pageable)).thenReturn(page);
        when(clientDTOMapper.apply(client)).thenReturn(dto);

        Page<ClientDTO> result = service.searchClients(search, pageable);
        assertEquals(1, result.getContent().size());
    }
}


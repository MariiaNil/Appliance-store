package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.dto.ClientDTO;
import com.epam.rd.autocode.assessment.appliances.mapper.ClientDTOMapper;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.model.Orders;
import com.epam.rd.autocode.assessment.appliances.repository.ClientRepository;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final EntityManager entityManager;
    private final ClientDTOMapper clientDTOMapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public Client createClient(Client client) {
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        return clientRepository.save(client);
    }

    @Override
    public ClientDTO getClientById(Long id) {
        return clientRepository.findById(id)
                .map(clientDTOMapper)
                .orElseThrow(() ->
                     new RuntimeException("Client not found"));
    }

    @Override
    public Page<ClientDTO> getClients(Pageable pageable) {
        Page<Client> clientPage = clientRepository.findAll(pageable);
        return clientPage.map(clientDTOMapper);
    }

    @Override
    public ClientDTO updateClient(Long id, ClientDTO clientDto) {
        return null;
    }

    @Override
    @Transactional
    public void deleteClient(Long id) {
        Client client = entityManager.find(Client.class, id);

        List<Orders> orders = entityManager.createQuery(
                "SELECT o FROM Orders o WHERE o.client = :client", Orders.class
        ).setParameter("client", client).getResultList();

        for (Orders order : orders) {
            order.setClient(null);
            entityManager.merge(order);
        }

        entityManager.remove(client);
        /*clientRepository.deleteById(id);*/
    }

    @Override
    public List<ClientDTO> getAllClientsList() {
        return clientRepository.findAll()
                .stream()
                .map(clientDTOMapper)
                .toList();
    }

    @Override
    public Page<ClientDTO> searchClients(String search, Pageable pageable) {
        return clientRepository.findByNameContainingIgnoreCase(search, pageable)
                .map(clientDTOMapper);
    }

    @Override
    @Transactional
    public void hashExistingPasswords() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Client> clientPage;

        do {
            clientPage = clientRepository.findAll(pageable);
            List<Client> clientsToUpdate = new ArrayList<>();
            for (Client client : clientPage.getContent()) {
                String rawPassword = client.getPassword();
                if (!rawPassword.startsWith("$2a$") && !rawPassword.startsWith("$2b$")) {
                    String hashedPassword = passwordEncoder.encode(rawPassword);
                    client.setPassword(hashedPassword);
                    clientsToUpdate.add(client);
                }
            }
            clientRepository.saveAll(clientsToUpdate);
            pageable = pageable.next();
        } while (clientPage.hasNext());
    }
}

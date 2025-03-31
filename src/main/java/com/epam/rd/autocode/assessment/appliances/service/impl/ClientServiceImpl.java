package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.dto.ClientDTO;
import com.epam.rd.autocode.assessment.appliances.mapper.ClientDTOMapper;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.repository.ClientRepository;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);
    private final ClientRepository clientRepository;
    private final ClientDTOMapper clientDTOMapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public Client createClient(Client client) {
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        logger.info("Creating client: {}", client);
        return clientRepository.save(client);
    }

    @Override
    public ClientDTO getClientById(Long id) {
        logger.info("Getting client by ID: {}", id);
        return clientRepository.findById(id)
                .map(clientDTOMapper)
                .orElseThrow(() -> {
                    logger.error("Client with ID {} not found", id);
                    return new RuntimeException("Client not found");
                });
    }

    @Override
    public Page<ClientDTO> getClients(Pageable pageable) {
        Page<Client> clientPage = clientRepository.findAll(pageable);
        logger.info("Clients found: {}", clientPage.getTotalElements());
        return clientPage.map(clientDTOMapper);
    }

    @Override
    public ClientDTO updateClient(Long id, ClientDTO clientDto) {
        return null;
    }

    @Override
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
        logger.info("Client with ID {} deleted", id);
    }

    @Override
    public List<ClientDTO> getAllClientsList() {
        logger.info("Getting all clients");
        return clientRepository.findAll()
                .stream()
                .map(clientDTOMapper)
                .toList();
    }

    @Override
    public Page<ClientDTO> searchClients(String search, Pageable pageable) {
        logger.info("Searching clients by name: {}", search);
        return clientRepository.findByNameContainingIgnoreCase(search, pageable)
                .map(clientDTOMapper);
    }

    @Override
    @Transactional
    public void hashExistingPasswords() {
        logger.info("Hashing existing passwords");
        Pageable pageable = PageRequest.of(0, 5);
        Page<Client> clientPage;

        do {
            clientPage = clientRepository.findAll(pageable);
            List<Client> clientsToUpdate = new ArrayList<>();
            logger.info("Updating {} clients", clientPage.getTotalElements());
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

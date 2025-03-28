package com.epam.rd.autocode.assessment.appliances.service.impl;

import com.epam.rd.autocode.assessment.appliances.dto.ClientDTO;
import com.epam.rd.autocode.assessment.appliances.mapper.ClientDTOMapper;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.repository.ClientRepository;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientDTOMapper clientDTOMapper;


    @Override
    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public ClientDTO getClientById(Long id) {
        return clientRepository.findById(id)
                .map(clientDTOMapper)
                .orElseThrow(() -> new RuntimeException("Client not found"));
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
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    @Override
    public Page<ClientDTO> searchClients(String search, Pageable pageable) {
        return clientRepository.findByNameContainingIgnoreCase(search, pageable)
                .map(clientDTOMapper);
    }
}

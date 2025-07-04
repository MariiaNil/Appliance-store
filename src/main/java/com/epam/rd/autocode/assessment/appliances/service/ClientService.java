package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.dto.ClientDTO;
import com.epam.rd.autocode.assessment.appliances.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ClientService {
    Client createClient(Client client);
    ClientDTO getClientById(Long id);
    Client getClientEntityById(Long id);
    Page<ClientDTO> getClients(Pageable pageable);
    void deleteClient(Long id);
    List<ClientDTO> getAllClientsList();
    Page<ClientDTO> searchClients(String search, Pageable pageable);
    void hashExistingPasswords();
}

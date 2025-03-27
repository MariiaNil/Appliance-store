package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.dto.ClientDTO;
import com.epam.rd.autocode.assessment.appliances.model.Client;

import java.util.List;

public interface ClientService {
    public Client createClient(Client client);
    public ClientDTO getClientById(Long id);
    public List<ClientDTO> getClients();
    public ClientDTO updateClient(Long id, ClientDTO clientDto);
    public void deleteClient(Long id);
}

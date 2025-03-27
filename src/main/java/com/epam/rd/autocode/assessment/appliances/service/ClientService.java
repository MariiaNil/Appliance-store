package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.model.Client;

import java.util.List;

public interface ClientService {
    public Client createClient(Client client);
    public Client getClientById(Long id);
    public List<Client> getClients();
    public Client updateClient(Long id, Client client);
    public void deleteClient(Long id);
}

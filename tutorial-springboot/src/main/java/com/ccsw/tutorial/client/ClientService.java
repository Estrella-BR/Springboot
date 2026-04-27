package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;

import java.util.List;

public interface ClientService {
    Client get(Long id);

    void save(Long id, ClientDto dto);

    void delete(Long id) throws Exception;

    List<Client> findAll();

}

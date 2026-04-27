package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientTest {

    @Mock
    private ClientRepository ClientRepository;

    @InjectMocks
    private ClientServiceImpl ClientService;

    @Test
    public void findAllShouldReturnAllCategories() {

        List<Client> list = new ArrayList<>();
        list.add(mock(Client.class));

        when(ClientRepository.findAll()).thenReturn(list);

        List<Client> categories = ClientService.findAll();

        assertNotNull(categories);
        assertEquals(1, categories.size());
    }

    public static final String CLIENT_NAME = "CAT1";

    @Test
    public void saveNotExistsClientIdShouldInsert() {

        ClientDto ClientDto = new ClientDto();
        ClientDto.setName(CLIENT_NAME);

        ArgumentCaptor<Client> Client = ArgumentCaptor.forClass(Client.class);

        ClientService.save(null, ClientDto);

        verify(ClientRepository).save(Client.capture());

        assertEquals(CLIENT_NAME, Client.getValue().getName());
    }

    public static final Long EXISTS_CLIENT_ID = 1L;

    @Test
    public void saveExistsClientIdShouldUpdate() {

        ClientDto ClientDto = new ClientDto();
        ClientDto.setName(CLIENT_NAME);

        Client Client = mock(Client.class);
        when(ClientRepository.findById(EXISTS_CLIENT_ID)).thenReturn(Optional.of(Client));

        ClientService.save(EXISTS_CLIENT_ID, ClientDto);

        verify(ClientRepository).save(Client);
    }

    @Test
    public void deleteExistsClientIdShouldDelete() throws Exception {

        Client Client = mock(Client.class);
        when(ClientRepository.findById(EXISTS_CLIENT_ID)).thenReturn(Optional.of(Client));

        ClientService.delete(EXISTS_CLIENT_ID);

        verify(ClientRepository).deleteById(EXISTS_CLIENT_ID);
    }

    public static final Long NOT_EXISTS_CLIENT_ID = 0L;

    @Test
    public void getExistsClientIdShouldReturnClient() {

        Client Client = mock(Client.class);
        when(Client.getId()).thenReturn(EXISTS_CLIENT_ID);
        when(ClientRepository.findById(EXISTS_CLIENT_ID)).thenReturn(Optional.of(Client));

        Client ClientResponse = ClientService.get(EXISTS_CLIENT_ID);

        assertNotNull(ClientResponse);
        assertEquals(EXISTS_CLIENT_ID, Client.getId());
    }

    @Test
    public void getNotExistsClientIdShouldReturnNull() {

        when(ClientRepository.findById(NOT_EXISTS_CLIENT_ID)).thenReturn(Optional.empty());

        Client Client = ClientService.get(NOT_EXISTS_CLIENT_ID);

        assertNull(Client);
    }

}
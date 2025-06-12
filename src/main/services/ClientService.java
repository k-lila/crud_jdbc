package main.services;

import main.dao.IClientDAO;
import main.domain.Client;
import main.services.generics.GenericService;

public class ClientService extends GenericService<Client, Long> implements IClientService{
    public ClientService(IClientDAO iClientDAO) {
        super(iClientDAO);
    }
}

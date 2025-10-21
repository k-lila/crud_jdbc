package crud.services;

import crud.dao.IClientDAO;
import crud.domain.Client;
import crud.services.generics.GenericService;

public class ClientService extends GenericService<Client, Long> implements IClientService{
    public ClientService(IClientDAO iClientDAO) {
        super(iClientDAO);
    }
}

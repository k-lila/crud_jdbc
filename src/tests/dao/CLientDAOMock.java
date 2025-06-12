package tests.dao;

import java.util.Collection;

import main.dao.IClientDAO;
import main.domain.Client;
import main.exceptions.DAOException;
import main.exceptions.PrimaryKeyNotFound;

public class CLientDAOMock implements IClientDAO {

    @Override
    public Boolean create(Client entity) throws PrimaryKeyNotFound, DAOException {
        return true;
    }

    @Override
    public Client read(Long valor) {
        Client client = new Client();
        client.setCpf(valor);
        return client;
    }

    @Override
    public Boolean update(Client entity) throws PrimaryKeyNotFound, DAOException {
        return true;
    }

    @Override
    public Boolean delete(Long valor) throws PrimaryKeyNotFound, DAOException {
        return true;
    }

    @Override
    public Collection<Client> all() throws DAOException, PrimaryKeyNotFound {
        return null;
    }

}

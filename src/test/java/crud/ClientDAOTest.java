package crud;

import java.sql.Timestamp;
import java.util.Collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import crud.dao.ClientDAO;
import crud.dao.IClientDAO;
import crud.domain.Client;
import crud.exceptions.DAOException;
import crud.exceptions.MoreThanOneRegisterException;
import crud.exceptions.PrimaryKeyNotFound;
import crud.exceptions.TableException;

public class ClientDAOTest {
    private IClientDAO clientDAO;
    private Client clientTest;
    private String nome;
    private Long cpf;
    private String cidade;
    private String end;
    private String estado;
    private Integer num;
    private Long tel;

    public ClientDAOTest() {
        this.clientDAO = new ClientDAO();
        this.nome = "teste";
        this.cpf = 1l;
        this.cidade = "cidade";
        this.end = "endereço";
        this.estado = "estado";
        this.num = 1;
        this.tel = 123123l;
        this.clientTest = new Client();
        clientTest.setName(nome);
        clientTest.setCpf(cpf);
        clientTest.setAddress(end);
        clientTest.setNumber(num);
        clientTest.setCity(cidade);
        clientTest.setState(estado);
        clientTest.setFone(tel);
        clientTest.setCreationDate(new Timestamp(System.currentTimeMillis()));
    }

    @AfterEach
    public void clear() throws PrimaryKeyNotFound, DAOException {
        Collection<Client> all = clientDAO.all();
        for (Client c : all) {
            try {
                clientDAO.delete(c.getCpf());
            } catch (DAOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void createEntryTest() throws PrimaryKeyNotFound, DAOException {
        Boolean resultado = clientDAO.create(clientTest);
        Assertions.assertTrue(resultado);
    }

    @Test
    public void searchEntryTest() throws PrimaryKeyNotFound, DAOException, TableException, MoreThanOneRegisterException {
        Boolean resultado = clientDAO.create(clientTest);
        Assertions.assertTrue(resultado);
        Client consultado = clientDAO.read(cpf);
        Assertions.assertNotNull(consultado);
    }

    @Test
    public void updateEntryTest() throws PrimaryKeyNotFound, DAOException, TableException, MoreThanOneRegisterException {
        Boolean resultado = clientDAO.create(clientTest);
        Assertions.assertTrue(resultado);
        Client oldClient = clientDAO.read(cpf);
        oldClient.setAddress("Novo");
        oldClient.setName("Novo");
        oldClient.setCity("Novo");
        oldClient.setFone(2l);
        oldClient.setNumber(2);
        oldClient.setState("NW");
        Boolean update = clientDAO.update(oldClient);
        Assertions.assertTrue(update);
        Client upClient = clientDAO.read(cpf);
        Assertions.assertEquals(upClient.getAddress(), "Novo");
        Assertions.assertEquals(upClient.getName(), "Novo");
        Assertions.assertEquals(upClient.getCity(), "Novo");
        Assertions.assertEquals(upClient.getFone(), Long.valueOf(2));
        Assertions.assertEquals(upClient.getNumber(), Integer.valueOf(2));
        Assertions.assertEquals(upClient.getState(), "NW");
    }

    @Test
    public void deleteEntryTest() throws PrimaryKeyNotFound, DAOException {
        Boolean resultado = clientDAO.create(clientTest);
        Assertions.assertTrue(resultado);
        Boolean del = clientDAO.delete(cpf);
        Assertions.assertTrue(del);
    }

    @Test
    public void searchAllTest() throws PrimaryKeyNotFound, DAOException {
        for (int i = 0; i < 10; i++) {
            Client _client = clientTest;
            _client.setCpf(cpf * i);
            clientDAO.create(_client);
        }
        Collection<Client> all = clientDAO.all();
        Assertions.assertEquals(10, all.size());
    }
}

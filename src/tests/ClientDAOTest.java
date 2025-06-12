package tests;

import java.sql.Timestamp;
import java.util.Collection;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import main.dao.ClientDAO;
import main.dao.IClientDAO;
import main.domain.Client;
import main.exceptions.DAOException;
import main.exceptions.MoreThanOneRegisterException;
import main.exceptions.PrimaryKeyNotFound;
import main.exceptions.TableException;

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
        Assert.assertTrue(resultado);
    }

    @Test
    public void searchEntryTest() throws PrimaryKeyNotFound, DAOException, TableException, MoreThanOneRegisterException {
        Boolean resultado = clientDAO.create(clientTest);
        Assert.assertTrue(resultado);
        Client consultado = clientDAO.read(cpf);
        Assert.assertNotNull(consultado);
    }

    @Test
    public void updateEntryTest() throws PrimaryKeyNotFound, DAOException, TableException, MoreThanOneRegisterException {
        Boolean resultado = clientDAO.create(clientTest);
        Assert.assertTrue(resultado);
        Client oldClient = clientDAO.read(cpf);
        oldClient.setAddress("Novo");
        oldClient.setName("Novo");
        oldClient.setCity("Novo");
        oldClient.setFone(2l);
        oldClient.setNumber(2);
        oldClient.setState("NW");
        Boolean update = clientDAO.update(oldClient);
        Assert.assertTrue(update);
        Client upClient = clientDAO.read(cpf);
        Assert.assertEquals(upClient.getAddress(), "Novo");
        Assert.assertEquals(upClient.getName(), "Novo");
        Assert.assertEquals(upClient.getCity(), "Novo");
        Assert.assertEquals(upClient.getFone(), Long.valueOf(2));
        Assert.assertEquals(upClient.getNumber(), Integer.valueOf(2));
        Assert.assertEquals(upClient.getState(), "NW");
    }

    @Test
    public void deleteEntryTest() throws PrimaryKeyNotFound, DAOException {
        Boolean resultado = clientDAO.create(clientTest);
        Assert.assertTrue(resultado);
        Boolean del = clientDAO.delete(cpf);
        Assert.assertTrue(del);
    }

    @Test
    public void searchAllTest() throws PrimaryKeyNotFound, DAOException {
        for (int i = 0; i < 10; i++) {
            Client _client = clientTest;
            _client.setCpf(cpf * i);
            clientDAO.create(_client);
        }
        Collection<Client> all = clientDAO.all();
        Assert.assertEquals(10, all.size());
    }
}

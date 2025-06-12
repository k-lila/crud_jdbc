package tests;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.dao.IClientDAO;
import main.domain.Client;
import main.exceptions.DAOException;
import main.exceptions.MoreThanOneRegisterException;
import main.exceptions.PrimaryKeyNotFound;
import main.exceptions.TableException;
import main.services.ClientService;
import main.services.IClientService;
import tests.dao.CLientDAOMock;

public class ClientServiceTest {
	private IClientService clientService;
	private Client cliente;
	
	public ClientServiceTest() {
		IClientDAO dao = new CLientDAOMock();
		clientService = new ClientService(dao);
	}

	@BeforeEach
	public void init() {
		cliente = new Client();
		cliente.setCpf(1l);
		cliente.setName("Name");
		cliente.setCity("Cidade");
		cliente.setAddress("Endereço");
		cliente.setState("State");
		cliente.setNumber(10);
		cliente.setFone(9l); 
	}

    @Test
	public void registerClientTest() throws PrimaryKeyNotFound, DAOException {
		Boolean retorno = clientService.register(cliente);
		Assert.assertTrue(retorno);
	}

	@Test
	public void searchClientTest() throws DAOException, PrimaryKeyNotFound, TableException, MoreThanOneRegisterException {
		Client clienteConsultado = clientService.search(cliente.getCpf());
		Assert.assertNotNull(clienteConsultado);
	}

	@Test
	public void editClientTest() throws PrimaryKeyNotFound, DAOException {
		cliente.setName("Novo");
        clientService.edit(cliente);
		Assert.assertEquals("Novo", cliente.getName());
	}

	@Test
	public void excludeClientTest() throws DAOException, PrimaryKeyNotFound {
		clientService.exclude(cliente.getCpf());
	}
}

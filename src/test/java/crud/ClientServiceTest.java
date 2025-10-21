package crud;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import crud.dao.CLientDAOMock;
import crud.dao.IClientDAO;
import crud.domain.Client;
import crud.exceptions.DAOException;
import crud.exceptions.MoreThanOneRegisterException;
import crud.exceptions.PrimaryKeyNotFound;
import crud.exceptions.TableException;
import crud.services.ClientService;
import crud.services.IClientService;


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
		Assertions.assertTrue(retorno);
	}

	@Test
	public void searchClientTest() throws DAOException, PrimaryKeyNotFound, TableException, MoreThanOneRegisterException {
		Client clienteConsultado = clientService.search(cliente.getCpf());
		Assertions.assertNotNull(clienteConsultado);
	}

	@Test
	public void editClientTest() throws PrimaryKeyNotFound, DAOException {
		cliente.setName("Novo");
        clientService.edit(cliente);
		Assertions.assertEquals("Novo", cliente.getName());
	}

	@Test
	public void excludeClientTest() throws DAOException, PrimaryKeyNotFound {
		clientService.exclude(cliente.getCpf());
	}
}

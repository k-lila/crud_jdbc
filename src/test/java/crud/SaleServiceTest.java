package crud;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import crud.dao.ClientDAO;
import crud.dao.IClientDAO;
import crud.dao.IProductDAO;
import crud.dao.ISaleDAO;
import crud.dao.IStockDAO;
import crud.dao.ProductDAO;
import crud.dao.SaleDAO;
import crud.dao.StockDAO;
import crud.dao.connection.ConnectionFactory;
import crud.domain.Client;
import crud.domain.Product;
import crud.domain.Sale;
import crud.domain.Sale.Status;
import crud.domain.Stock;
import crud.exceptions.DAOException;
import crud.exceptions.MoreThanOneRegisterException;
import crud.exceptions.PrimaryKeyNotFound;
import crud.exceptions.ServiceException;
import crud.exceptions.TableException;
import crud.services.ISaleService;
import crud.services.SaleService;

public class SaleServiceTest {


    private IClientDAO clientDAO;
    private IProductDAO productDAO;


    private ISaleDAO saleDAO;
    private IStockDAO stockDAO;
    private ISaleService saleService;
    private Client clientTest;
    private Product productTest;
    private Stock stockTest;
    private Sale saleTest;

    public SaleServiceTest() {

        clientDAO = new ClientDAO();
        productDAO = new ProductDAO();
        saleDAO = new SaleDAO();
        stockDAO = new StockDAO();
        saleService = new SaleService(saleDAO, stockDAO);

        clientTest = new Client();
        clientTest.setName("Nome");
        clientTest.setCpf(1l);
        clientTest.setAddress("Endereço");
        clientTest.setNumber(1);
        clientTest.setCity("Cidade");
        clientTest.setState("Estado");
        clientTest.setFone(9l);
        clientTest.setCreationDate(new Timestamp(System.currentTimeMillis()));
        productTest = new Product();
        productTest.setCode("P1");
        productTest.setName("Produto");
        productTest.setDescription("Descrição");
        productTest.setPrice(BigDecimal.TEN);
        stockTest = new Stock();
        stockTest.setProduct(productTest);
        stockTest.setQuantity(10);
        saleTest = new Sale();
        saleTest.setCode("V1");
        saleTest.setStatus(Status.INICIADA);
        saleTest.setDate(Instant.now());
    }

    protected Connection getConnection() throws DAOException {
        try {
            return ConnectionFactory.getConnection();
        } catch (SQLException e) {
            throw new DAOException("ERRO AO TENTAR CONECTAR COM O BANCO DE DADOS", e);
        }
    }

	protected void closeConnection(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        try {
			if (resultSet != null && !resultSet.isClosed()) {
				resultSet.close();
			}
			if (statement != null && !statement.isClosed()) {
				statement.close();
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
    }

    @BeforeEach
    public void setup() throws PrimaryKeyNotFound, DAOException {
        clientDAO.create(clientTest);
        productDAO.create(productTest);
        stockDAO.create(stockTest);
    }

	@AfterEach
	private void clean() throws DAOException, SQLException {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = getConnection();
			String[] tables = {
				"TRUNCATE TABLE TB_PRODUCT_QUANTITY;",
				"TRUNCATE TABLE TB_SALES CASCADE;",
				"TRUNCATE TABLE TB_PRODUCTS CASCADE;",
				"TRUNCATE TABLE TB_CLIENTS CASCADE;"
			};
			for (String sql : tables) {
				statement = connection.prepareStatement(sql);
				statement.executeUpdate();
				statement.close();
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection(connection, statement, null);
		}
	}

    @Test
    public void closeSaleTest() throws PrimaryKeyNotFound, DAOException, TableException, MoreThanOneRegisterException, ServiceException {
        Client client = clientDAO.read(clientTest.getCpf());
        Product product = productDAO.read(productTest.getCode());
        saleTest.setClient(client);
        saleTest.addProduct(product, 1);
        Boolean newSale = saleService.register(saleTest);
        Assertions.assertTrue(newSale);
        Boolean closeSale = saleService.closeSale(saleTest);
        Assertions.assertTrue(closeSale);
        Integer quantity = stockDAO.searchByProductCode(productTest.getCode()).getQuantity();
        Assertions.assertEquals(Integer.valueOf(9), quantity);
        Sale closedSale = saleService.search(saleTest.getCode());
        Assertions.assertEquals(saleTest.getCode(), closedSale.getCode());
        Assertions.assertEquals(saleTest.getClient().getCpf(), closedSale.getClient().getCpf());
        Assertions.assertEquals(Status.CONCLUIDA, closedSale.getStatus());
    }

    // @Test
    // public void uncloseSaleTest() throws PrimaryKeyNotFound, DAOException, TableException, MoreThanOneRegisterException, ServiceException {
    //     Client client = clientDAO.read(clientTest.getCpf());
    //     Product product = productDAO.read(productTest.getCode());
    //     saleTest.setClient(client);
    //     saleTest.addProduct(product, 15);
    //     Boolean newSale = saleService.register(saleTest);
    //     Assertions.assertTrue(newSale);
    //     ServiceException exception = assertThrows(ServiceException.class, () -> {
    //         saleService.closeSale(saleTest);
    //     });
    //     Assertions.assertEquals("ESTOQUE INSUFICIENTE", exception.getMessage());
    // }

    @Test
    public void cancelSaleTest() throws PrimaryKeyNotFound, DAOException, TableException, MoreThanOneRegisterException, ServiceException {
        Client client = clientDAO.read(clientTest.getCpf());
        Product product = productDAO.read(productTest.getCode());
        saleTest.setClient(client);
        saleTest.addProduct(product, 5);
        Boolean newSale = saleService.register(saleTest);
        Assertions.assertTrue(newSale);
        Boolean closeSale = saleService.closeSale(saleTest);
        Assertions.assertTrue(closeSale);
        Sale closedSale = saleService.search(saleTest.getCode());
        Integer quantity = stockDAO.searchByProductCode(productTest.getCode()).getQuantity();
        Assertions.assertEquals(Integer.valueOf(5), quantity);
        Boolean cancel = saleService.cancelSale(closedSale);
        Assertions.assertTrue(cancel);
        quantity = stockDAO.searchByProductCode(productTest.getCode()).getQuantity();
        Assertions.assertEquals(Integer.valueOf(10), quantity);
    }
}

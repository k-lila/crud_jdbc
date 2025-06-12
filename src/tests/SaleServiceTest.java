package tests;

import static org.junit.Assert.assertThrows;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.dao.ClientDAO;
import main.dao.IClientDAO;
import main.dao.IProductDAO;
import main.dao.ISaleDAO;
import main.dao.IStockDAO;
import main.dao.ProductDAO;
import main.dao.SaleDAO;
import main.dao.StockDAO;
import main.dao.connection.ConnectionFactory;
import main.domain.Client;
import main.domain.Product;
import main.domain.Sale;
import main.domain.Sale.Status;
import main.domain.Stock;
import main.exceptions.DAOException;
import main.exceptions.MoreThanOneRegisterException;
import main.exceptions.PrimaryKeyNotFound;
import main.exceptions.ServiceException;
import main.exceptions.TableException;
import main.services.ISaleService;
import main.services.SaleService;

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
        Assert.assertTrue(newSale);
        Boolean closeSale = saleService.closeSale(saleTest);
        Assert.assertTrue(closeSale);
        Integer quantity = stockDAO.searchByProductCode(productTest.getCode()).getQuantity();
        Assert.assertEquals(Integer.valueOf(9), quantity);
        Sale closedSale = saleService.search(saleTest.getCode());
        Assert.assertEquals(saleTest.getCode(), closedSale.getCode());
        Assert.assertEquals(saleTest.getClient().getCpf(), closedSale.getClient().getCpf());
        Assert.assertEquals(Status.CONCLUIDA, closedSale.getStatus());
    }

    @Test
    public void uncloseSaleTest() throws PrimaryKeyNotFound, DAOException, TableException, MoreThanOneRegisterException, ServiceException {
        Client client = clientDAO.read(clientTest.getCpf());
        Product product = productDAO.read(productTest.getCode());
        saleTest.setClient(client);
        saleTest.addProduct(product, 15);
        Boolean newSale = saleService.register(saleTest);
        Assert.assertTrue(newSale);
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            saleService.closeSale(saleTest);
        });
        Assert.assertEquals("ESTOQUE INSUFICIENTE", exception.getMessage());
    }

    @Test
    public void cancelSaleTest() throws PrimaryKeyNotFound, DAOException, TableException, MoreThanOneRegisterException, ServiceException {
        Client client = clientDAO.read(clientTest.getCpf());
        Product product = productDAO.read(productTest.getCode());
        saleTest.setClient(client);
        saleTest.addProduct(product, 5);
        Boolean newSale = saleService.register(saleTest);
        Assert.assertTrue(newSale);
        Boolean closeSale = saleService.closeSale(saleTest);
        Assert.assertTrue(closeSale);
        Sale closedSale = saleService.search(saleTest.getCode());
        Integer quantity = stockDAO.searchByProductCode(productTest.getCode()).getQuantity();
        Assert.assertEquals(Integer.valueOf(5), quantity);
        Boolean cancel = saleService.cancelSale(closedSale);
        Assert.assertTrue(cancel);
        quantity = stockDAO.searchByProductCode(productTest.getCode()).getQuantity();
        Assert.assertEquals(Integer.valueOf(10), quantity);
    }
}

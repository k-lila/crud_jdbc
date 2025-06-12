package tests;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.dao.ClientDAO;
import main.dao.IClientDAO;
import main.dao.IProductDAO;
import main.dao.ISaleDAO;
import main.dao.ProductDAO;
import main.dao.SaleDAO;
import main.dao.connection.ConnectionFactory;
import main.domain.Client;
import main.domain.Product;
import main.domain.Sale;
import main.domain.Sale.Status;
import main.exceptions.DAOException;
import main.exceptions.MoreThanOneRegisterException;
import main.exceptions.PrimaryKeyNotFound;
import main.exceptions.TableException;
public class SaleDAOTest {

	private ISaleDAO saleDAO;
	private IClientDAO clientDAO;
	private IProductDAO productDAO;
	private Client clientTest;
	private Product productTest;
	private Product productTest2;

	public SaleDAOTest() {
		clientTest = new Client();
		clientTest.setName("Teste Nome");
		clientTest.setCpf(1l);
		clientTest.setAddress("Endereço");
		clientTest.setCity("Cidade");
		clientTest.setState("Estado");
		clientTest.setNumber(1);
		clientTest.setFone(9l);
		clientTest.setCreationDate(new Timestamp(System.currentTimeMillis()));
		productTest = new Product();
		productTest.setCode("P1");
		productTest.setName("Nome Produto");
		productTest.setPrice(BigDecimal.valueOf(1.99));
		productTest.setDescription("Descrição");
		productTest.setCategory("Categoria");
		productTest2 = new Product();
		productTest2.setCode("P2");
		productTest2.setName("Nome Produto");
		productTest2.setPrice(BigDecimal.TEN);
		productTest2.setDescription("Descrição");
		productTest2.setCategory("Categoria");
		saleDAO = new SaleDAO();
		clientDAO = new ClientDAO();
		productDAO = new ProductDAO();
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

	private Sale createSale(String code) {
		Sale sale = new Sale();
		sale.setCode(code);
		sale.setDate(Instant.now());
		sale.setClient(clientTest);
		sale.setStatus(Status.INICIADA);
		sale.addProduct(productTest,2);
		return sale;
	}

	@BeforeEach
	private void init() throws PrimaryKeyNotFound, DAOException {
		clientDAO.create(clientTest);
		productDAO.create(productTest);
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
    public void createEntryTest() throws PrimaryKeyNotFound, DAOException {
		Sale sale = createSale("T1");
		Boolean retorno = saleDAO.create(sale);
		Assert.assertTrue(retorno);
    }

    @Test
    public void searchEntryTest() throws PrimaryKeyNotFound, DAOException, TableException, MoreThanOneRegisterException {
		Sale sale = createSale("T2");
		Boolean retorno = saleDAO.create(sale);
		Assert.assertTrue(retorno);
		Sale consultado = saleDAO.read("T2");
		Assert.assertNotNull(consultado);
		Assert.assertEquals(BigDecimal.valueOf(3.98), consultado.getTotalPrice());
		Assert.assertEquals(Integer.valueOf(2), consultado.getTotalQuantity());
    }

    @Test
    public void addSameProductTest() throws PrimaryKeyNotFound, DAOException, TableException, MoreThanOneRegisterException  {
		Sale sale = createSale("T3");
		Boolean retorno = saleDAO.create(sale);
		Assert.assertTrue(retorno);
		Sale consultado = saleDAO.read("T3");
		Assert.assertNotNull(consultado);
		consultado.addProduct(productTest, 2);
		Boolean update = saleDAO.update(consultado);
		Assert.assertTrue(update);
		Sale consultado2 = saleDAO.read("T3");
		Assert.assertEquals(BigDecimal.valueOf(7.96), consultado2.getTotalPrice());
		Assert.assertEquals(Integer.valueOf(4), consultado2.getTotalQuantity());
    }

	@Test
    public void removeSameProductTest() throws PrimaryKeyNotFound, DAOException, TableException, MoreThanOneRegisterException  {
		Sale sale = createSale("T4");
		Boolean retorno = saleDAO.create(sale);
		Assert.assertTrue(retorno);
		Sale consultado = saleDAO.read("T4");
		Assert.assertNotNull(consultado);
		consultado.removeProduct(productTest, 1);
		Boolean update = saleDAO.update(consultado);
		Assert.assertTrue(update);
		Sale consultado2 = saleDAO.read("T4");
		Assert.assertEquals(BigDecimal.valueOf(1.99), consultado2.getTotalPrice());
		Assert.assertEquals(Integer.valueOf(1), consultado2.getTotalQuantity());
    }

	@Test
    public void addOtherProductTest() throws PrimaryKeyNotFound, DAOException, TableException, MoreThanOneRegisterException  {
		Sale sale = createSale("T5");
		Boolean retorno = saleDAO.create(sale);
		Assert.assertTrue(retorno);
		Sale consultado = saleDAO.read("T5");
		Assert.assertNotNull(consultado);
		productDAO.create(productTest2);
		consultado.addProduct(productTest2, 2);
		Boolean update = saleDAO.update(consultado);
		Assert.assertTrue(update);
		Sale consultado2 = saleDAO.read("T5");
		Assert.assertEquals(BigDecimal.valueOf(23.98), consultado2.getTotalPrice());
		Assert.assertEquals(Integer.valueOf(4), consultado2.getTotalQuantity());
    }

	@Test
    public void removeBothProductsTest() throws PrimaryKeyNotFound, DAOException, TableException, MoreThanOneRegisterException  {
		Sale sale = createSale("T6");
		Boolean retorno = saleDAO.create(sale);
		Assert.assertTrue(retorno);
		Sale consultado = saleDAO.read("T6");
		Assert.assertNotNull(consultado);
		productDAO.create(productTest2);
		consultado.addProduct(productTest2, 2);
		Boolean update = saleDAO.update(consultado);
		Assert.assertTrue(update);
		Sale consultado2 = saleDAO.read("T6");
		consultado2.removeProduct(productTest, 1);
		consultado2.removeProduct(productTest2, 1);
		Boolean update2 = saleDAO.update(consultado2);
		Assert.assertTrue(update2);
		Sale consultado3 = saleDAO.read("T6");
		Assert.assertEquals(BigDecimal.valueOf(11.99), consultado3.getTotalPrice());
		Assert.assertEquals(Integer.valueOf(2), consultado3.getTotalQuantity());
    }

	@Test
	public void removeAllProductsTest() throws PrimaryKeyNotFound, DAOException, TableException, MoreThanOneRegisterException {
		Sale sale = createSale("T7");
		productDAO.create(productTest2);
		sale.addProduct(productTest2, 2);
		Boolean retorno = saleDAO.create(sale);
		Assert.assertTrue(retorno);
		Sale consultado = saleDAO.read("T7");
		consultado.removeAllProducts();
		Boolean update = saleDAO.update(consultado);
		Assert.assertTrue(update);
	}

	@Test
	public void closeSaleTest() throws PrimaryKeyNotFound, DAOException, TableException, MoreThanOneRegisterException {
		Sale sale = createSale("T8");
		Boolean retorno = saleDAO.create(sale);
		Assert.assertTrue(retorno);
		Boolean close = saleDAO.closeSale(sale);
		Assert.assertTrue(close);
		Sale consultado = saleDAO.read("T8");
		Assert.assertNotNull(consultado);
		Assert.assertEquals(BigDecimal.valueOf(3.98), consultado.getTotalPrice());
		Assert.assertEquals(Integer.valueOf(2), consultado.getTotalQuantity());
		Assert.assertEquals(Status.CONCLUIDA, consultado.getStatus());
	}

	@Test
	public void tryModifyClosedSale() throws PrimaryKeyNotFound, DAOException, TableException, MoreThanOneRegisterException {
		Sale sale = createSale("T9");
		Boolean retorno = saleDAO.create(sale);
		Assert.assertTrue(retorno);
		Boolean close = saleDAO.closeSale(sale);
		Assert.assertTrue(close);
		Sale consultado = saleDAO.read("T9");
		UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
		() -> consultado.addProduct(productTest, 2)
		);
		Assertions.assertEquals("IMPOSSÍVEL ALTERAR VENDA FINALIZADA", exception.getMessage());
	}

	@Test
	public void cancelSaleTest() throws PrimaryKeyNotFound, DAOException, TableException, MoreThanOneRegisterException {
		Sale sale = createSale("T10");
		Boolean retorno = saleDAO.create(sale);
		Assert.assertTrue(retorno);
		Boolean close = saleDAO.closeSale(sale);
		Assert.assertTrue(close);
		Sale consultado = saleDAO.read("T10");
		Assert.assertNotNull(consultado);
		Assert.assertEquals(BigDecimal.valueOf(3.98), consultado.getTotalPrice());
		Assert.assertEquals(Integer.valueOf(2), consultado.getTotalQuantity());
		Assert.assertEquals(Status.CONCLUIDA, consultado.getStatus());
		Boolean cancel = saleDAO.cancelSale(sale);
		Assert.assertTrue(cancel);
		Sale consultado2 = saleDAO.read("T10");
		Assert.assertEquals(Status.CANCELADA, consultado2.getStatus());
	}

	@Test
	public void allSalesTest() throws PrimaryKeyNotFound, DAOException {
		Sale sale = createSale("T11");
		Boolean retorno = saleDAO.create(sale);
		Assert.assertTrue(retorno);
		sale = createSale("T12");
		retorno = saleDAO.create(sale);
		Assert.assertTrue(retorno);
		sale = createSale("T13");
		retorno = saleDAO.create(sale);
		Assert.assertTrue(retorno);
		Collection<Sale> all = saleDAO.all();
		Assert.assertEquals(3, all.size());
	}

}

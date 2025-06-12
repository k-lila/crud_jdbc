package tests;

import java.math.BigDecimal;
import java.util.Collection;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.dao.IProductDAO;
import main.dao.IStockDAO;
import main.dao.ProductDAO;
import main.dao.StockDAO;
import main.domain.Product;
import main.domain.Stock;
import main.exceptions.DAOException;
import main.exceptions.MoreThanOneRegisterException;
import main.exceptions.PrimaryKeyNotFound;
import main.exceptions.TableException;

public class StockDAOTest {

    private Product productTest;
	private Product productTest2;
    private IStockDAO stockDAO;
    private IProductDAO productDAO;


    @BeforeEach
    public void init() throws PrimaryKeyNotFound, DAOException {
        productDAO.create(productTest);
        productDAO.create(productTest2);
    }

    @AfterEach
    public void clean() throws PrimaryKeyNotFound, DAOException {
        productDAO.delete(productTest.getCode());
        productDAO.delete(productTest2.getCode());
    }


    public StockDAOTest() {
        productTest = new Product();
		productTest.setCode("P1");
		productTest.setName("Nome Produto");
		productTest.setPrice(BigDecimal.valueOf(1.99));
		productTest.setDescription("Descrição");
        productTest.setCategory("Category");
		productTest2 = new Product();
		productTest2.setCode("P2");
		productTest2.setName("Nome Produto");
		productTest2.setPrice(BigDecimal.TEN);
		productTest2.setDescription("Descrição");
        productTest2.setCategory("Category");
        stockDAO = new StockDAO();
        productDAO = new ProductDAO();
    }

    @Test
    public void createStockEntryTest() throws PrimaryKeyNotFound, DAOException {
        Stock entry = new Stock();
        entry.setProduct(productTest);
        entry.setQuantity(10);
        Boolean add = stockDAO.create(entry);
        Assert.assertTrue(add);
    }

    @Test
    public void readStockByProductCodeTest() throws PrimaryKeyNotFound, DAOException, TableException, MoreThanOneRegisterException {
        Stock entry = new Stock();
        entry.setProduct(productTest);
        entry.setQuantity(10);
        Boolean add = stockDAO.create(entry);
        Assert.assertTrue(add);
        Stock consultado = stockDAO.searchByProductCode(productTest.getCode());
        Assert.assertNotNull(consultado);
        Assert.assertEquals(Integer.valueOf(10), consultado.getQuantity());
        Assert.assertEquals(productTest.getCode(), consultado.getProduct().getCode());
    }

    @Test
    public void readStockTest() throws PrimaryKeyNotFound, DAOException, TableException, MoreThanOneRegisterException {
        Stock entry = new Stock();
        entry.setProduct(productTest);
        entry.setQuantity(10);
        Boolean add = stockDAO.create(entry);
        Assert.assertTrue(add);
        Stock consultado = stockDAO.searchByProductCode(productTest.getCode());
        Assert.assertNotNull(consultado);
        consultado = stockDAO.read(consultado.getId());
        Assert.assertNotNull(consultado);
        Assert.assertEquals(Integer.valueOf(10), consultado.getQuantity());
        Assert.assertEquals(productTest.getCode(), consultado.getProduct().getCode());
    }

    @Test
    public void modifyStockTest() throws PrimaryKeyNotFound, DAOException, TableException, MoreThanOneRegisterException {
        Stock entry = new Stock();
        entry.setProduct(productTest);
        entry.setQuantity(10);
        Boolean add = stockDAO.create(entry);
        Assert.assertTrue(add);
        Stock consultado = stockDAO.searchByProductCode(productTest.getCode());
        Assert.assertNotNull(consultado);
        consultado.setProduct(productTest2);
        consultado.setQuantity(0);
        Boolean update = stockDAO.update(consultado);
        Assert.assertTrue(update);
        consultado = stockDAO.read(consultado.getId());
        Assert.assertNotNull(update);
        Assert.assertEquals(Integer.valueOf(0), consultado.getQuantity());
        Assert.assertEquals(productTest2.getCode(), consultado.getProduct().getCode());
    }  

    @Test
    public void addQuantityTest() throws PrimaryKeyNotFound, DAOException, TableException, MoreThanOneRegisterException {
        Stock entry = new Stock();
        entry.setProduct(productTest);
        entry.setQuantity(10);
        Boolean add = stockDAO.create(entry);
        Assert.assertTrue(add);
        Stock consultado = stockDAO.searchByProductCode(productTest.getCode());
        Assert.assertNotNull(consultado);
        consultado.setQuantity(consultado.getQuantity() + 10);
        Boolean update = stockDAO.update(consultado);
        Assert.assertTrue(update);
        consultado = stockDAO.read(consultado.getId());
        Assert.assertEquals(Integer.valueOf(20), consultado.getQuantity());
    }

    @Test
    public void removeQuantityTest() throws PrimaryKeyNotFound, DAOException, TableException, MoreThanOneRegisterException {
        Stock entry = new Stock();
        entry.setProduct(productTest);
        entry.setQuantity(10);
        Boolean add = stockDAO.create(entry);
        Assert.assertTrue(add);
        Stock consultado = stockDAO.searchByProductCode(productTest.getCode());
        Assert.assertNotNull(consultado);
        consultado.setQuantity(consultado.getQuantity() - 5);
        Boolean update = stockDAO.update(consultado);
        Assert.assertTrue(update);
        consultado = stockDAO.read(consultado.getId());
        Assert.assertEquals(Integer.valueOf(5), consultado.getQuantity());        
    }

    @Test
    public void deleteTest() throws PrimaryKeyNotFound, DAOException {
        Stock entry = new Stock();
        entry.setProduct(productTest);
        entry.setQuantity(10);
        Boolean add = stockDAO.create(entry);
        Assert.assertTrue(add);
        Stock consultado = stockDAO.searchByProductCode(productTest.getCode());
        Assert.assertNotNull(consultado);
        Boolean delete = stockDAO.delete(consultado.getId());
        Assert.assertTrue(delete);
        consultado = stockDAO.searchByProductCode(productTest.getCode());
        Assert.assertNull(consultado);
    }

    @Test
    public void allTest() throws PrimaryKeyNotFound, DAOException {
        Stock e1 = new Stock();
        Stock e2 = new Stock();
        e1.setProduct(productTest);
        e2.setProduct(productTest2);
        e1.setQuantity(0);
        e2.setQuantity(0);
        Boolean add1 = stockDAO.create(e1);
        Boolean add2 = stockDAO.create(e2);
        Assert.assertTrue(add1);
        Assert.assertTrue(add2);
        Collection<Stock> all = stockDAO.all();
        Assert.assertEquals(2, all.size());
    }
}

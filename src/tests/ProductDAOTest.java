package tests;

import java.math.BigDecimal;
import java.util.Collection;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import main.dao.IProductDAO;
import main.dao.ProductDAO;
import main.domain.Product;
import main.exceptions.DAOException;
import main.exceptions.MoreThanOneRegisterException;
import main.exceptions.PrimaryKeyNotFound;
import main.exceptions.TableException;

public class ProductDAOTest {
    private IProductDAO productDao;
    private Product produtoTeste;
    private String code;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;

    public ProductDAOTest() {
        this.productDao = new ProductDAO();
        this.code = "T1";
        this.name = "nome teste";
        this.description = "teste testando";
        this.price = BigDecimal.valueOf(1.99);
        this.category = "categoria";
        this.produtoTeste = new Product();
        produtoTeste.setCode(code);
        produtoTeste.setName(name);
        produtoTeste.setDescription(description);
        produtoTeste.setCategory(category);
        produtoTeste.setPrice(price);
    }

    @AfterEach
    public void clear() throws PrimaryKeyNotFound, DAOException {
        Collection<Product> all = productDao.all();
        for (Product p : all) {
            try {
                productDao.delete(p.getCode());
            } catch (DAOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void createEntryTest() throws PrimaryKeyNotFound, DAOException {
        Boolean resultado = productDao.create(produtoTeste);
        Assert.assertTrue(resultado);
    }

    @Test
    public void searchEntryTest() throws PrimaryKeyNotFound, DAOException, TableException, MoreThanOneRegisterException {
        Boolean resultado = productDao.create(produtoTeste);
        Assert.assertTrue(resultado);
        Product consultado = productDao.read(code);
        Assert.assertNotNull(consultado);
    }

    @Test
    public void updateEntryTest() throws PrimaryKeyNotFound, DAOException, TableException, MoreThanOneRegisterException  {
        Boolean resultado = productDao.create(produtoTeste);
        Assert.assertTrue(resultado);
        Product consultado = productDao.read(code);
        Assert.assertEquals(produtoTeste.getName(), consultado.getName());
        Assert.assertEquals(produtoTeste.getDescription(), consultado.getDescription());
        Assert.assertEquals(produtoTeste.getCategory(), consultado.getCategory());
        Assert.assertEquals(produtoTeste.getPrice(), consultado.getPrice());
        consultado.setName("Novo");
        consultado.setDescription("Novo");
        consultado.setCategory("Outra");
        consultado.setPrice(BigDecimal.valueOf(10.01));
        Boolean update = productDao.update(consultado);
        Assert.assertTrue(update);
        Product upProduct = productDao.read(code);
        Assert.assertEquals("Novo", upProduct.getName());
        Assert.assertEquals("Novo", upProduct.getDescription());
        Assert.assertEquals("Outra", upProduct.getCategory());
        Assert.assertEquals(BigDecimal.valueOf(10.01), upProduct.getPrice());
    }

    @Test
    public void deleteEntryTest() throws PrimaryKeyNotFound, DAOException{
        Boolean resultado = productDao.create(produtoTeste);
        Assert.assertTrue(resultado);
        Boolean del = productDao.delete(code);
        Assert.assertTrue(del);
    }

    @Test
    public void searchAllTest() throws PrimaryKeyNotFound, DAOException {
        for (int i = 1; i < 10; i++) {
            Product _product = produtoTeste;
            _product.setCode(String.format("T%d", i));
            productDao.create(_product);
        }
        Collection<Product> all = productDao.all();
        Assert.assertEquals(9, all.size());
    }
}

package crud;

import java.math.BigDecimal;
import java.util.Collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import crud.dao.IProductDAO;
import crud.dao.ProductDAO;
import crud.domain.Product;
import crud.exceptions.DAOException;
import crud.exceptions.MoreThanOneRegisterException;
import crud.exceptions.PrimaryKeyNotFound;
import crud.exceptions.TableException;

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
        Assertions.assertTrue(resultado);
    }

    @Test
    public void searchEntryTest() throws PrimaryKeyNotFound, DAOException, TableException, MoreThanOneRegisterException {
        Boolean resultado = productDao.create(produtoTeste);
        Assertions.assertTrue(resultado);
        Product consultado = productDao.read(code);
        Assertions.assertNotNull(consultado);
    }

    @Test
    public void updateEntryTest() throws PrimaryKeyNotFound, DAOException, TableException, MoreThanOneRegisterException  {
        Boolean resultado = productDao.create(produtoTeste);
        Assertions.assertTrue(resultado);
        Product consultado = productDao.read(code);
        Assertions.assertEquals(produtoTeste.getName(), consultado.getName());
        Assertions.assertEquals(produtoTeste.getDescription(), consultado.getDescription());
        Assertions.assertEquals(produtoTeste.getCategory(), consultado.getCategory());
        Assertions.assertEquals(produtoTeste.getPrice(), consultado.getPrice());
        consultado.setName("Novo");
        consultado.setDescription("Novo");
        consultado.setCategory("Outra");
        consultado.setPrice(BigDecimal.valueOf(10.01));
        Boolean update = productDao.update(consultado);
        Assertions.assertTrue(update);
        Product upProduct = productDao.read(code);
        Assertions.assertEquals("Novo", upProduct.getName());
        Assertions.assertEquals("Novo", upProduct.getDescription());
        Assertions.assertEquals("Outra", upProduct.getCategory());
        Assertions.assertEquals(BigDecimal.valueOf(10.01), upProduct.getPrice());
    }

    @Test
    public void deleteEntryTest() throws PrimaryKeyNotFound, DAOException{
        Boolean resultado = productDao.create(produtoTeste);
        Assertions.assertTrue(resultado);
        Boolean del = productDao.delete(code);
        Assertions.assertTrue(del);
    }

    @Test
    public void searchAllTest() throws PrimaryKeyNotFound, DAOException {
        for (int i = 1; i < 10; i++) {
            Product _product = produtoTeste;
            _product.setCode(String.format("T%d", i));
            productDao.create(_product);
        }
        Collection<Product> all = productDao.all();
        Assertions.assertEquals(9, all.size());
    }
}

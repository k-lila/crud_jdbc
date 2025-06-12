package tests;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.dao.IProductDAO;
import main.domain.Product;
import main.exceptions.DAOException;
import main.exceptions.MoreThanOneRegisterException;
import main.exceptions.PrimaryKeyNotFound;
import main.exceptions.TableException;
import main.services.IProductService;
import main.services.ProductService;
import tests.dao.ProductDAOMock;

public class ProductServiceTest {

    private IProductService produtoService;
	private Product produto;
	
	public ProductServiceTest() {
		IProductDAO dao = new ProductDAOMock();
		produtoService = new ProductService(dao);
	}
	
	@BeforeEach
	public void init() {
		produto = new Product();
		produto.setCode("A1");
		produto.setDescription("Produto 1");
		produto.setName("Produto 1");
		produto.setPrice(BigDecimal.TEN);
	}

    @Test
    public void registerProductTest() throws DAOException, PrimaryKeyNotFound {
        Boolean retorno = produtoService.register(produto);
        Assert.assertTrue(retorno);
    }

    @Test
	public void searchProductTest() throws DAOException, PrimaryKeyNotFound, TableException, MoreThanOneRegisterException {
		Product produtor = this.produtoService.search(produto.getCode());
		Assert.assertNotNull(produtor);
	}

    @Test
    public void editProductTest() throws DAOException, PrimaryKeyNotFound {
        produto.setName("Novo");
        produtoService.edit(produto);
        Assert.assertEquals("Novo", produto.getName());
    }

	@Test
	public void excludeProductTest() throws DAOException, PrimaryKeyNotFound {
		Boolean delete = produtoService.exclude(produto.getCode());
        Assert.assertTrue(delete);
	}
}

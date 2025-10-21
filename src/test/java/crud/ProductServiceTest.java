package crud;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import crud.dao.IProductDAO;
import crud.domain.Product;
import crud.exceptions.DAOException;
import crud.exceptions.MoreThanOneRegisterException;
import crud.exceptions.PrimaryKeyNotFound;
import crud.exceptions.TableException;
import crud.services.IProductService;
import crud.services.ProductService;
import crud.dao.ProductDAOMock;

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
        Assertions.assertTrue(retorno);
    }

    @Test
	public void searchProductTest() throws DAOException, PrimaryKeyNotFound, TableException, MoreThanOneRegisterException {
		Product produtor = this.produtoService.search(produto.getCode());
		Assertions.assertNotNull(produtor);
	}

    @Test
    public void editProductTest() throws DAOException, PrimaryKeyNotFound {
        produto.setName("Novo");
        produtoService.edit(produto);
        Assertions.assertEquals("Novo", produto.getName());
    }

	@Test
	public void excludeProductTest() throws DAOException, PrimaryKeyNotFound {
		Boolean delete = produtoService.exclude(produto.getCode());
        Assertions.assertTrue(delete);
	}
}

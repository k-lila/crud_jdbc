package crud.services;

import crud.dao.IProductDAO;
import crud.domain.Product;
import crud.services.generics.GenericService;

public class ProductService extends GenericService<Product, String> implements IProductService {

    public ProductService(IProductDAO iProductDAO) {
        super(iProductDAO);
    }
}

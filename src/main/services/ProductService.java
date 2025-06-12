package main.services;

import main.dao.IProductDAO;
import main.domain.Product;
import main.services.generics.GenericService;

public class ProductService extends GenericService<Product, String> implements IProductService {

    public ProductService(IProductDAO iProductDAO) {
        super(iProductDAO);
    }
}

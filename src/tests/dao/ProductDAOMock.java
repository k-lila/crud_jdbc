package tests.dao;

import java.util.Collection;

import main.dao.IProductDAO;
import main.domain.Product;
import main.exceptions.DAOException;
import main.exceptions.MoreThanOneRegisterException;
import main.exceptions.PrimaryKeyNotFound;
import main.exceptions.TableException;

public class ProductDAOMock implements IProductDAO {

    @Override
    public Boolean create(Product entity) throws PrimaryKeyNotFound, DAOException {
        return true;
    }

    @Override
    public Product read(String valor)
            throws PrimaryKeyNotFound, DAOException, TableException, MoreThanOneRegisterException {
        Product product = new Product();
        product.setCode(valor);       
        return product;
    }

    @Override
    public Boolean update(Product entity) throws PrimaryKeyNotFound, DAOException {
        return true;
    }

    @Override
    public Boolean delete(String valor) throws PrimaryKeyNotFound, DAOException {
        return true;
    }

    @Override
    public Collection<Product> all() throws DAOException, PrimaryKeyNotFound {
        return null;
    }

}

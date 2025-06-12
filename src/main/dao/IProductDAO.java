package main.dao;

import main.dao.generics.IGenericDAO;
import main.domain.Product;

public interface IProductDAO extends IGenericDAO<Product, String> {
}

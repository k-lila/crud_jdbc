package main.dao;

import main.dao.generics.IGenericDAO;
import main.domain.Stock;
import main.exceptions.DAOException;

public interface IStockDAO extends IGenericDAO<Stock, Long> {
    public Stock searchByProductCode(String code) throws DAOException;
    public Boolean checkQuantity(String code, Integer quantity) throws DAOException;
}

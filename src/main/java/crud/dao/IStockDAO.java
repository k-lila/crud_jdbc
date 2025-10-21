package crud.dao;

import crud.dao.generics.IGenericDAO;
import crud.domain.Stock;
import crud.exceptions.DAOException;

public interface IStockDAO extends IGenericDAO<Stock, Long> {
    public Stock searchByProductCode(String code) throws DAOException;
    public Boolean checkQuantity(String code, Integer quantity) throws DAOException;
}

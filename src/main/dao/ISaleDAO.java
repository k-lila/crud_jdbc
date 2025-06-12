package main.dao;

import main.dao.generics.IGenericDAO;
import main.domain.Sale;
import main.exceptions.DAOException;
import main.exceptions.PrimaryKeyNotFound;

public interface ISaleDAO extends IGenericDAO<Sale, String> {
    public boolean closeSale(Sale sale) throws PrimaryKeyNotFound, DAOException;
    public boolean cancelSale(Sale sale) throws PrimaryKeyNotFound, DAOException;
}

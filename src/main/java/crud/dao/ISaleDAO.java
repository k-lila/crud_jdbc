package crud.dao;

import crud.dao.generics.IGenericDAO;
import crud.domain.Sale;
import crud.exceptions.DAOException;
import crud.exceptions.PrimaryKeyNotFound;

public interface ISaleDAO extends IGenericDAO<Sale, String> {
    public boolean closeSale(Sale sale) throws PrimaryKeyNotFound, DAOException;
    public boolean cancelSale(Sale sale) throws PrimaryKeyNotFound, DAOException;
}

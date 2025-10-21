package crud.services;

import crud.domain.Sale;
import crud.exceptions.DAOException;
import crud.exceptions.PrimaryKeyNotFound;
import crud.exceptions.ServiceException;
import crud.services.generics.IGenericService;

public interface ISaleService extends IGenericService<Sale, String> {
    public boolean closeSale(Sale sale) throws PrimaryKeyNotFound, DAOException, ServiceException;
    public boolean cancelSale(Sale sale) throws PrimaryKeyNotFound, DAOException;
}

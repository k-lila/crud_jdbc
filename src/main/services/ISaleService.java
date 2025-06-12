package main.services;

import main.domain.Sale;
import main.exceptions.DAOException;
import main.exceptions.PrimaryKeyNotFound;
import main.exceptions.ServiceException;
import main.services.generics.IGenericService;

public interface ISaleService extends IGenericService<Sale, String> {
    public boolean closeSale(Sale sale) throws PrimaryKeyNotFound, DAOException, ServiceException;
    public boolean cancelSale(Sale sale) throws PrimaryKeyNotFound, DAOException;
}

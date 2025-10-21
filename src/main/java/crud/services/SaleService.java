package crud.services;

import crud.dao.ISaleDAO;
import crud.dao.IStockDAO;
import crud.domain.ProductQuantity;
import crud.domain.Sale;
import crud.domain.Stock;
import crud.domain.Sale.Status;
import crud.exceptions.DAOException;
import crud.exceptions.PrimaryKeyNotFound;
import crud.exceptions.ServiceException;
import crud.services.generics.GenericService;

public class SaleService extends GenericService<Sale, String> implements ISaleService {

    private IStockDAO stockDAO;

    public SaleService(ISaleDAO iSaleDAO, IStockDAO iStockDAO) {
        super(iSaleDAO);
        this.stockDAO = iStockDAO; 
    }

    @Override
    public boolean closeSale(Sale sale) throws PrimaryKeyNotFound, DAOException, ServiceException {
        for (ProductQuantity pq : sale.getProducts()) {
            Stock stock = stockDAO.searchByProductCode(pq.getProduct().getCode());
            if (stock.getQuantity() < pq.getQuantity()) {
                throw new ServiceException("ESTOQUE INSUFICIENTE", null);
            } else {
                stock.setQuantity(stock.getQuantity() - pq.getQuantity());
                stockDAO.update(stock);
            }
        }
        return ((ISaleDAO) this.dao).closeSale(sale);
    }

    @Override
    public boolean cancelSale(Sale sale) throws PrimaryKeyNotFound, DAOException {
        if (sale.getStatus() == Status.CONCLUIDA) {
            for (ProductQuantity pq : sale.getProducts()) {
                Stock stock = stockDAO.searchByProductCode(pq.getProduct().getCode());
                stock.setQuantity(stock.getQuantity() + pq.getQuantity());
                stockDAO.update(stock);
            }
        }
        return ((ISaleDAO) this.dao).cancelSale(sale);
    }
}

package main.services;

import main.dao.ISaleDAO;
import main.dao.IStockDAO;
import main.domain.ProductQuantity;
import main.domain.Sale;
import main.domain.Stock;
import main.domain.Sale.Status;
import main.exceptions.DAOException;
import main.exceptions.PrimaryKeyNotFound;
import main.exceptions.ServiceException;
import main.services.generics.GenericService;

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

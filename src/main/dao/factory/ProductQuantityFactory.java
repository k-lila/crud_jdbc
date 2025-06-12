package main.dao.factory;

import java.sql.ResultSet;
import java.sql.SQLException;

import main.domain.Product;
import main.domain.ProductQuantity;

public class ProductQuantityFactory {
    public static ProductQuantity convert(ResultSet resultSet) throws SQLException {
        Product p = ProductFactory.convert(resultSet);
        ProductQuantity pq = new ProductQuantity();
        pq.setId(resultSet.getLong("ID"));
        pq.setProduct(p);
        pq.setQuantity(resultSet.getInt("QUANTITY"));
        pq.setTotalPrice(resultSet.getBigDecimal("TOTAL_PRICE"));
        return pq;
    }
}

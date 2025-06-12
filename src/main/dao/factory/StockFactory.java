package main.dao.factory;

import java.sql.ResultSet;
import java.sql.SQLException;

import main.domain.Product;
import main.domain.Stock;

public class StockFactory {
    public static Stock convert(ResultSet resultSet) throws SQLException {
        Product p = new Product();
        p.setId(resultSet.getLong("PRODUCT_ID"));
        p.setCode(resultSet.getString("CODE"));
        p.setName(resultSet.getString("NAME"));
        p.setDescription(resultSet.getString("DESCRIPTION"));
        p.setPrice(resultSet.getBigDecimal("PRICE"));
        Stock s = new Stock();
        s.setId(resultSet.getLong("ID"));
        s.setProduct(p);
        s.setQuantity(resultSet.getInt("QUANTITY"));
        return s;
    }
}

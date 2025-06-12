package main.dao.factory;

import java.sql.ResultSet;
import java.sql.SQLException;

import main.domain.Product;

public class ProductFactory {

    public static Product convert(ResultSet resultSet) throws SQLException {
        Product p = new Product();
        p.setId(resultSet.getLong("ID_PRODUCT"));
        p.setCode(resultSet.getString("CODE"));
        p.setName(resultSet.getString("NAME"));
        p.setDescription(resultSet.getString("DESCRIPTION"));
        p.setPrice(resultSet.getBigDecimal("PRICE"));
        p.setCategory(resultSet.getString("CATEGORY"));
        return p;
    }
}

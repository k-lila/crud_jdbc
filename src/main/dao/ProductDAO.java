package main.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import main.dao.generics.GenericDAO;
import main.domain.Product;

public class ProductDAO extends GenericDAO<Product, String> implements IProductDAO {

    @Override
    public Class<Product> getClassType() {
        return Product.class;
    }

    @Override
    protected String getInsertionQuery() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO TB_PRODUCTS ");
        stringBuilder.append("(ID, CODE, NAME, DESCRIPTION, PRICE, CATEGORY)");
        stringBuilder.append("VALUES (nextval('sq_products'),?,?,?,?,?)");
        return stringBuilder.toString();
    }    

    @Override
    protected void setInsertionQueryParams(PreparedStatement statement, Product entity) throws SQLException {
        statement.setString(1, entity.getCode());
        statement.setString(2, entity.getName());
        statement.setString(3, entity.getDescription());
        statement.setBigDecimal(4, entity.getPrice());
        statement.setString(5, entity.getCategory());
    }

    @Override
    protected String getSelectQuery() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM TB_PRODUCTS WHERE CODE = ?");
        return stringBuilder.toString();
    }

    @Override
    protected void setSelectQueryParams(PreparedStatement statement, String value) throws SQLException {
        statement.setString(1, value);
    }

    @Override
    protected String getUpdateQuery() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("UPDATE TB_PRODUCTS ");
		stringBuilder.append("SET CODE = ?,");
		stringBuilder.append("NAME = ?,");
		stringBuilder.append("DESCRIPTION = ?,");
		stringBuilder.append("PRICE = ?,");
        stringBuilder.append("CATEGORY = ?");
		stringBuilder.append(" WHERE CODE = ?");
		return stringBuilder.toString();    
    }

    @Override
    protected void setUpdateQueryParams(PreparedStatement statement, Product entity) throws SQLException {
        statement.setString(1, entity.getCode());
		statement.setString(2, entity.getName());
		statement.setString(3, entity.getDescription());
		statement.setBigDecimal(4, entity.getPrice());
        statement.setString(5, entity.getCategory());
		statement.setString(6, entity.getCode());
    }

    @Override
    protected String getExcludeQuery() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DELETE FROM TB_PRODUCTS WHERE CODE = ?");
        return stringBuilder.toString();
    }

    @Override
    protected void setExcludeQueryParams(PreparedStatement statement, String value) throws SQLException {
        statement.setString(1, value);
    }

}

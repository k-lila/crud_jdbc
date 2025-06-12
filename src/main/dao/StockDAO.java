package main.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.dao.generics.GenericDAO;
import main.domain.Product;
import main.domain.Stock;
import main.exceptions.DAOException;

public class StockDAO extends GenericDAO<Stock, Long> implements IStockDAO {

    @Override
    public Class<Stock> getClassType() {
        return Stock.class;
    }

    @Override
    protected String getInsertionQuery() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO TB_STOCK (ID, ID_PRODUCT_FK, QUANTITY) VALUES (nextval('sq_stock'), ?, ?)");
        return stringBuilder.toString();
    }

    @Override
    protected void setInsertionQueryParams(PreparedStatement statement, Stock entity) throws SQLException {
        statement.setLong(1, entity.getProduct().getId());
        statement.setInt(2, entity.getQuantity());
    }

    @Override
    protected String getSelectQuery() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT S.ID, S.QUANTITY, P.ID AS PRODUCT_ID, P.CODE, P.NAME, P.DESCRIPTION, P.PRICE, P.CATEGORY ");
        stringBuilder.append("FROM TB_STOCK S ");
        stringBuilder.append("JOIN TB_PRODUCTS P ON S.ID_PRODUCT_FK = P.ID ");
        stringBuilder.append("WHERE S.ID = ?;");
        return stringBuilder.toString();
    }

    @Override
    protected String getSelectAllQuery() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT S.ID, S.QUANTITY, P.ID AS PRODUCT_ID, P.CODE, P.NAME, P.DESCRIPTION, P.PRICE, P.CATEGORY ");
        stringBuilder.append("FROM TB_STOCK S ");
        stringBuilder.append("JOIN TB_PRODUCTS P ON S.ID_PRODUCT_FK = P.ID ");
        return stringBuilder.toString();
    }

    @Override
    protected void setSelectQueryParams(PreparedStatement statement, Long value) throws SQLException {
        statement.setLong(1, value);
    }

    @Override
    protected String getUpdateQuery() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("UPDATE TB_STOCK ");
		stringBuilder.append("SET ID_PRODUCT_FK = ?,");
		stringBuilder.append("QUANTITY = ? ");
        stringBuilder.append("WHERE ID = ?;");
		return stringBuilder.toString();
    }

    @Override
    protected void setUpdateQueryParams(PreparedStatement statement, Stock entity) throws SQLException {
        statement.setLong(1, entity.getProduct().getId());
        statement.setInt(2, entity.getQuantity());
        statement.setLong(3, entity.getId());
    }

    @Override
    protected String getExcludeQuery() {
        return "DELETE FROM TB_STOCK WHERE ID = ?;";
    }

    @Override
    protected void setExcludeQueryParams(PreparedStatement statement, Long value) throws SQLException {
        statement.setLong(1, value);
    }

    private String selectByCodeQuery() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT S.ID, S.QUANTITY, P.ID AS PRODUCT_ID, P.CODE, P.NAME, P.DESCRIPTION, P.PRICE, P.CATEGORY ");
        stringBuilder.append("FROM TB_STOCK S ");
        stringBuilder.append("JOIN TB_PRODUCTS P ON S.ID_PRODUCT_FK = P.ID ");
        stringBuilder.append("WHERE  P.CODE = '%s';");
        return stringBuilder.toString();
    }

    @Override
    public Stock searchByProductCode(String code) throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(String.format(selectByCodeQuery(), code));
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
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
        } catch (SQLException e) {
            throw new DAOException("ERRO AO CONSULTAR OBJETO NO ESTOQUE", e);
        } finally {
            closeConnection(connection, statement, resultSet);
        }
        return null;
    }

    @Override
    public Boolean checkQuantity(String code, Integer quantity) throws DAOException {
        Stock stock = searchByProductCode(code);
        if (stock == null) {
            return false;
        } else {
            return searchByProductCode(code).getQuantity() >= quantity;
        }
    }
}

package main.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import main.dao.factory.ProductQuantityFactory;
import main.dao.factory.SaleFactory;
import main.dao.generics.GenericDAO;
import main.domain.ProductQuantity;
import main.domain.Sale;
import main.domain.Sale.Status;
import main.exceptions.DAOException;
import main.exceptions.MoreThanOneRegisterException;
import main.exceptions.PrimaryKeyNotFound;
import main.exceptions.TableException;

public class SaleDAO extends GenericDAO<Sale, String> implements ISaleDAO {

    @Override
    public Class<Sale> getClassType() {
        return Sale.class;
    }

    private StringBuilder sqlBaseSelect() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT V.ID AS ID_SALES, V.CODE, V.TOTAL_PRICE, V.DATE, V.STATUS, ");
        stringBuilder.append("C.ID AS ID_CLIENTS, C.NAME, C.CPF, C.FONE, C.ADDRESS, C.NUMBER, C.CITY, C.STATE, C.CREATION_DATE ");
        stringBuilder.append("FROM TB_SALES V ");
        stringBuilder.append("INNER JOIN TB_CLIENTS C ON V.ID_CLIENT_FK = C.ID ");
        return stringBuilder;
    }

    private void searchAssociation(Connection connection, Sale sale) throws DAOException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SELECT PQ.ID, PQ.QUANTITY, PQ.TOTAL_PRICE, ");
            stringBuilder.append("P.ID AS ID_PRODUCT, P.CODE, P.NAME, P.DESCRIPTION, P.PRICE, P.CATEGORY ");
            stringBuilder.append("FROM TB_PRODUCT_QUANTITY PQ ");
            stringBuilder.append("INNER JOIN TB_PRODUCTS P ON P.ID = PQ.ID_PRODUCT_FK ");
            stringBuilder.append("WHERE PQ.ID_SALE_FK = ?");
            statement = connection.prepareStatement(stringBuilder.toString());
            statement.setLong(1, sale.getId());
            resultSet = statement.executeQuery();
            Set<ProductQuantity> products = new HashSet<>();
            while(resultSet.next()) {
                ProductQuantity pq = ProductQuantityFactory.convert(resultSet);
                products.add(pq);
            }
            sale.setProducts(products);
            if (sale.getStatus() != Status.CONCLUIDA) {
                sale.recalculateTotal();
            }
        } catch (SQLException e) {
            throw new DAOException("ERRO AO CONSULTAR OBJETO", e);
        } finally {
            closeConnection(null, statement, resultSet);
        }
    }

    private String getInsertionQueryPQ() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO TB_PRODUCT_QUANTITY ");
        stringBuilder.append("(ID, ID_PRODUCT_FK, ID_SALE_FK, QUANTITY, TOTAL_PRICE)");
        stringBuilder.append("VALUES (nextval('sq_product_quantity'),?,?,?,?)");
        return stringBuilder.toString();
    }

    private void setInsertionQueryParamsPQ(PreparedStatement statement, Sale sale, ProductQuantity pq) throws SQLException {
        statement.setLong(1, pq.getProduct().getId());
        statement.setLong(2, sale.getId());
        statement.setInt(3, pq.getQuantity());
        statement.setBigDecimal(4, pq.getTotalPrice());
    }

    @Override
    public Boolean create(Sale entity) throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(getInsertionQuery(), Statement.RETURN_GENERATED_KEYS);
            setInsertionQueryParams(statement, entity);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = statement.getGeneratedKeys()) {
                    if (rs.next()) {
                        entity.setId(rs.getLong(1));
                    }
                }
                for (ProductQuantity pq : entity.getProducts()) {
                    statement = connection.prepareStatement(getInsertionQueryPQ());
                    setInsertionQueryParamsPQ(statement, entity, pq);
                    rowsAffected = statement.executeUpdate();
                }
                return true;
            }
        } catch (SQLException e) {
            throw new DAOException("ERRO AO CADASTRAR O OBJETO", e);
        } finally {
            closeConnection(connection, statement, null);
        }
        return false;
    }

    @Override
    public Sale read(String value) throws DAOException, TableException, MoreThanOneRegisterException {
        StringBuilder stringBuilder = sqlBaseSelect();
        stringBuilder.append("WHERE V.CODE = ? ");
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(stringBuilder.toString());
            setSelectQueryParams(statement, value);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Sale sale = SaleFactory.convert(resultSet);
                searchAssociation(connection, sale);
                return sale;
            }
            return null;
        } catch (SQLException e) {
            throw new DAOException("ERRO AO CONSULTAR OBJETO", e);
        } finally {
            closeConnection(connection, statement, resultSet);
        }
    }

    @Override
    public Boolean update(Sale sale) throws DAOException {
    Connection connection = null;
    PreparedStatement statement = null;
    try {
        connection = getConnection();
        connection.setAutoCommit(false);
        statement = connection.prepareStatement(getUpdateQuery());
        setUpdateQueryParams(statement, sale);
        int rowsAffected = statement.executeUpdate();
        statement.close();
        if (rowsAffected == 0) {
            connection.rollback();
            return false;
        }
        statement = connection.prepareStatement("DELETE FROM TB_PRODUCT_QUANTITY WHERE ID_SALE_FK = ?");
        statement.setLong(1, sale.getId());
        statement.executeUpdate();
        statement.close();
        for (ProductQuantity pq : sale.getProducts()) {
            PreparedStatement statement2 = connection.prepareStatement(getInsertionQueryPQ());
            setInsertionQueryParamsPQ(statement2, sale, pq);
            statement2.executeUpdate();
            statement2.close();
        }
        connection.commit();
        return true;

    } catch (SQLException e) {
        try {
            if (connection != null) connection.rollback();
        } catch (SQLException rollbackEx) {
            throw new DAOException("ERRO AO FAZER ROLLBACK", rollbackEx);
        }
        throw new DAOException("ERRO AO ATUALIZAR A VENDA", e);
    } finally {
        closeConnection(connection, statement, null);
    }
}

    @Override
    public Boolean delete(String value) throws DAOException {
        throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA");
    }

    @Override
    public Collection<Sale> all() throws DAOException, PrimaryKeyNotFound {
        List<Sale> list = new ArrayList<>();
        StringBuilder stringBuilder = sqlBaseSelect();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(stringBuilder.toString());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Sale sale = SaleFactory.convert(resultSet);
                searchAssociation(connection, sale);
                list.add(sale);
            }
        } catch (SQLException e) {
            throw new DAOException("ERRO AO CONSULTAR O OBJETO", e);
        } finally {
            closeConnection(connection, statement, resultSet);
        }
        return list;
    }

    @Override
    public String getInsertionQuery() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO TB_SALES ");
        stringBuilder.append("(ID, CODE, ID_CLIENT_FK, TOTAL_PRICE, DATE, STATUS)");
        stringBuilder.append("VALUES (nextval('sq_sales'),?,?,?,?,?)");
        return stringBuilder.toString();
    }

    @Override
    public void setInsertionQueryParams(PreparedStatement statement, Sale entity) throws SQLException {
        statement.setString(1, entity.getCode());
        statement.setLong(2, entity.getClient().getId());
        statement.setBigDecimal(3, entity.getTotalPrice());
        statement.setTimestamp(4, Timestamp.from(entity.getDate()));
        statement.setString(5, entity.getStatus().name());
    }

    @Override
    public String getSelectQuery() {
        throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA");
    }

    @Override
    public void setSelectQueryParams(PreparedStatement statement, String value) throws SQLException {
        statement.setString(1, value);
    }

    @Override
    public String getUpdateQuery() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UPDATE TB_SALES SET ");
        stringBuilder.append("CODE = ?, ID_CLIENT_FK = ?, TOTAL_PRICE = ?, DATE = ?, STATUS = ? WHERE ID = ?");
        return stringBuilder.toString();
    }

    @Override
    public void setUpdateQueryParams(PreparedStatement statement, Sale sale) throws SQLException {
        statement.setString(1, sale.getCode());
        statement.setLong(2, sale.getClient().getId());
        statement.setBigDecimal(3, sale.getTotalPrice());
        statement.setTimestamp(4, Timestamp.from(sale.getDate()));
        statement.setString(5, sale.getStatus().name());
        statement.setLong(6, sale.getId());
    }

    @Override
    public String getExcludeQuery() {
        throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA");
    }

    @Override
    public void setExcludeQueryParams(PreparedStatement statement, String value) throws SQLException {
        throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA");
    }

    @Override
    public boolean closeSale(Sale sale) throws PrimaryKeyNotFound, DAOException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            String sql = "UPDATE TB_SALES SET STATUS = ? WHERE ID = ?";
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, Status.CONCLUIDA.name());
            statement.setLong(2, sale.getId());
            int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            throw new DAOException("ERRO AO ATUALIZAR O OBJETO", e);
        } finally {
            closeConnection(connection, statement, null);
        }
    }

    @Override
    public boolean cancelSale(Sale sale) throws PrimaryKeyNotFound, DAOException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            String sql = "UPDATE TB_SALES SET STATUS = ? WHERE ID = ?";
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, Status.CANCELADA.name());
            statement.setLong(2, sale.getId());
            int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            throw new DAOException("ERRO AO ATUALIZAR O OBJETO", e);
        } finally {
            closeConnection(connection, statement, null);
        }
    }

}

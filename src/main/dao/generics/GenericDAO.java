package main.dao.generics;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.lang.reflect.Field;

import main.dao.Persistent;
import main.dao.connection.ConnectionFactory;
import main.domain.Product;
import main.domain.Stock;
import main.annotations.KeyType;
import main.annotations.ProjectColumn;
import main.annotations.ProjectTable;
import main.exceptions.DAOException;
import main.exceptions.ElementTypeNotFound;
import main.exceptions.MoreThanOneRegisterException;
import main.exceptions.PrimaryKeyNotFound;
import main.exceptions.TableException;

public abstract class GenericDAO<T extends Persistent, E extends Serializable> implements IGenericDAO<T, E> {
    public abstract Class<T> getClassType();
    protected abstract String getInsertionQuery();
    protected abstract String getSelectQuery();
    protected abstract String getUpdateQuery();
    protected abstract String getExcludeQuery();
    protected abstract void setInsertionQueryParams(PreparedStatement statement, T entity) throws SQLException;
    protected abstract void setSelectQueryParams(PreparedStatement statement, E value) throws SQLException;
    protected abstract void setUpdateQueryParams(PreparedStatement statement, T entity) throws SQLException;
    protected abstract void setExcludeQueryParams(PreparedStatement statement, E value) throws SQLException;

    public GenericDAO() {}

    protected Connection getConnection() throws DAOException {
        try {
            return ConnectionFactory.getConnection();
        } catch (SQLException e) {
            throw new DAOException("ERRO AO TENTAR CONECTAR COM O BANCO DE DADOS", e);
        }
    }

    protected void closeConnection(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        try {
			if (resultSet != null && !resultSet.isClosed()) {
				resultSet.close();
			}
			if (statement != null && !statement.isClosed()) {
				statement.close();
			}
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
    }

    private String getKeyFieldName(Class clazz) throws PrimaryKeyNotFound {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(KeyType.class) && field.isAnnotationPresent(ProjectColumn.class)) {
                ProjectColumn col = field.getAnnotation(ProjectColumn.class);
                return col.dbName();
            }
        }
        return null;
    }

    private String getTableName() throws TableException {
        if (getClassType().isAnnotationPresent(ProjectTable.class)) {
            ProjectTable table = getClassType().getAnnotation(ProjectTable.class);
            return table.value();
        } else {
            throw new TableException("TABELA NO TIPO" + getClassType().getName() + "NÃO FOI ENCONTRADA");
        }
    }

    private Long validateMoreThanOne(E value) throws MoreThanOneRegisterException, TableException, PrimaryKeyNotFound, DAOException {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Long count = null;
        try {
            statement = connection.prepareStatement("SELECT count(*) FROM" + getTableName() + "WHERE" + getKeyFieldName(getClassType()) + " = ?");
            setSelectQueryParams(statement, value);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getLong(1);
                if (count > 1) {
                    throw new MoreThanOneRegisterException("ENCONTRADO MAIS DE UM REGISTRO DE" + getTableName());
                }
            }
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection, statement, resultSet);
        }
        return count;
    }

    private void setValueByType(T entity, Method method, Class<?> classField, ResultSet resultSet, String fieldName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException, PrimaryKeyNotFound, ElementTypeNotFound {
        if (classField.equals(Integer.class)) {
            Integer val = resultSet.getInt(fieldName);
            method.invoke(entity, val);
        } else if (classField.equals(Long.class)) {
            Long val = resultSet.getLong(fieldName);
            method.invoke(entity, val);
        } else if (classField.equals(Double.class)) {
            Double val =  resultSet.getDouble(fieldName);
            method.invoke(entity, val);
        } else if (classField.equals(Short.class)) {
            Short val =  resultSet.getShort(fieldName);
            method.invoke(entity, val);
        } else if (classField.equals(BigDecimal.class)) {
            BigDecimal val =  resultSet.getBigDecimal(fieldName);
            method.invoke(entity, val);
        } else if (classField.equals(String.class)) {
            String val =  resultSet.getString(fieldName);
            method.invoke(entity, val);
        } else if (classField.equals(Timestamp.class)) {
            Timestamp val = resultSet.getTimestamp(fieldName);
            method.invoke(entity, val);
        } else if (classField.equals(Product.class)) {
            Product p = new Product();
            p.setId(resultSet.getLong("ID"));
            p.setCode(resultSet.getString("CODE"));
            p.setName(resultSet.getString("NAME"));
            p.setDescription(resultSet.getString("DESCRIPTION"));
            p.setPrice(resultSet.getBigDecimal("PRICE"));
            p.setCategory(resultSet.getString("CATEGORY"));
            method.invoke(entity, p);
        } else if (classField.equals(Stock.class)) {
            Product p = new Product();
            p.setId(resultSet.getLong("PRODUCT_ID"));
            p.setCode(resultSet.getString("CODE"));
            p.setName(resultSet.getString("NAME"));
            p.setDescription(resultSet.getString("DESCRIPTION"));
            p.setPrice(resultSet.getBigDecimal("PRICE"));
            p.setCategory(resultSet.getString("CATEGORY"));
            Stock s = new Stock();
            s.setId(resultSet.getLong("ID"));
            s.setProduct(p);
            s.setQuantity(resultSet.getInt("QUANTITY"));
            method.invoke(entity, s);
        } else {
            throw new ElementTypeNotFound("TIPO DE CLASSE NÃO CONHECIDO: " + classField);
        }
	}

    protected String getSelectAllQuery() throws TableException {
        return "SELECT * FROM " + getTableName();
    }

    @Override
    public Boolean create(T entity) throws DAOException {
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
                        Persistent persinstent = (Persistent) entity;
                        persinstent.setId(rs.getLong(1));
                    }
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
    public T read(E value) throws DAOException, TableException, MoreThanOneRegisterException {
        try {
            validateMoreThanOne(value);
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(getSelectQuery());
            setSelectQueryParams(statement, value);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                T entity = getClassType().getConstructor().newInstance();
                Field[] fields = entity.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(ProjectColumn.class)) {
                        ProjectColumn col = field.getAnnotation(ProjectColumn.class);
                        String dbName = col.dbName();
                        String javaSetName = col.setJavaName();
                        Class<?> classField = field.getType();
                        try {
                            Method method = entity.getClass().getMethod(javaSetName, classField);
                            setValueByType(entity, method, classField, resultSet, dbName);
                        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                            throw new DAOException("ERRO CONSULTANDO O OBJETO", e);
                        } catch (ElementTypeNotFound e) {
                            throw new DAOException("ERRO CONSULTANDO O OBJETO", e);
                        }
                    }
                }
                return entity;
            }
        } catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException | PrimaryKeyNotFound e) {
            throw new DAOException("ERRO CONSULTANDO O OBJETO", e);
        }
        return null;
    }

    @Override
    public Boolean update(T entity) throws DAOException {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(getUpdateQuery());
            setUpdateQueryParams(statement, entity);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DAOException("ERRO AO ALTERAR O OBJETO", e);
        } finally {
            closeConnection(connection, statement, null);
        }
    }

    @Override
    public Boolean delete(E value) throws DAOException {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(getExcludeQuery());
            setExcludeQueryParams(statement, value);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DAOException("ERRO AO EXCLUIR O OBJETO", e);
        } finally {
            closeConnection(connection, statement, null);
        }
    }

    @Override
    public Collection<T> all() throws DAOException, PrimaryKeyNotFound {
        List<T> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(getSelectAllQuery());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                T entity = getClassType().getConstructor().newInstance();
                Field[] fields = entity.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(ProjectColumn.class)) {
                        ProjectColumn col = field.getAnnotation(ProjectColumn.class);
                        String dbName = col.dbName();
                        String javaSetName = col.setJavaName();
                        Class<?> classField = field.getType();
                        try {
                            Method method = entity.getClass().getMethod(javaSetName, classField);
                            setValueByType(entity, method, classField, resultSet, dbName);
                        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                            throw new DAOException("ERRO AO LISTAR OS OBJETOS", e);
                        } catch (ElementTypeNotFound e) {
                            throw new DAOException("ERRO AO LISTAR OS OBJETOS", e);
                        }
                    }
                }
                list.add(entity);
            }
        } catch (SQLException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | TableException e) {
            throw new DAOException("ERRO AO LISTAR OS OBJETOS", e);
        } finally {
            closeConnection(connection, statement, resultSet);
        }
        return list;
    }
}

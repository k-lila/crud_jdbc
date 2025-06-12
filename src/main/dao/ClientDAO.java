package main.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import main.dao.generics.GenericDAO;
import main.domain.Client;

public class ClientDAO extends GenericDAO<Client, Long> implements IClientDAO {

    public ClientDAO() {
        super();
    }

    @Override
    public Class<Client> getClassType() {
        return Client.class;
    }

    @Override
    public String getInsertionQuery() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO TB_CLIENTS ");
        stringBuilder.append("(ID, NAME, CPF, FONE, ADDRESS, NUMBER, CITY, STATE, CREATION_DATE) ");
        stringBuilder.append("VALUES (nextval('sq_clients'),?,?,?,?,?,?,?,?)");
        return stringBuilder.toString();
    }

    @Override
    public void setInsertionQueryParams(PreparedStatement statement, Client entity) throws SQLException {
        statement.setString(1, entity.getName());
        statement.setLong(2, entity.getCpf());
        statement.setLong(3, entity.getFone());
        statement.setString(4, entity.getAddress());
        statement.setLong(5, entity.getNumber());
        statement.setString(6, entity.getCity());
        statement.setString(7, entity.getState());
        statement.setTimestamp(8, entity.getCreationDate());
    }

    @Override
    public String getSelectQuery() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM TB_CLIENTS WHERE CPF = ?");
        return stringBuilder.toString();
    }

    @Override
    public void setSelectQueryParams(PreparedStatement statement, Long value) throws SQLException {
        statement.setLong(1, value);
    }

    @Override
    public String getUpdateQuery() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UPDATE TB_CLIENTS ");
        stringBuilder.append("SET NAME = ?,");
        stringBuilder.append("FONE = ?,");
        stringBuilder.append("ADDRESS = ?,");
        stringBuilder.append("NUMBER = ?,");
        stringBuilder.append("CITY = ?,");
        stringBuilder.append("STATE = ?,");
        stringBuilder.append("CREATION_DATE = ? ");
        stringBuilder.append("WHERE CPF = ?");
        return stringBuilder.toString();
    }

    @Override
    public void setUpdateQueryParams(PreparedStatement statement, Client entity) throws SQLException {
        statement.setString(1, entity.getName());
        statement.setLong(2, entity.getNumber());
        statement.setString(3, entity.getAddress());
        statement.setInt(4, entity.getNumber());
        statement.setString(5, entity.getCity());
        statement.setString(6, entity.getState());
        statement.setTimestamp(7, entity.getCreationDate());
        statement.setLong(8, entity.getCpf());
    }

    @Override
    public String getExcludeQuery() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DELETE FROM TB_CLIENTS WHERE CPF = ?");
        return stringBuilder.toString();
    }

    @Override
    public void setExcludeQueryParams(PreparedStatement statement, Long value) throws SQLException {
        statement.setLong(1, value);
    }
}

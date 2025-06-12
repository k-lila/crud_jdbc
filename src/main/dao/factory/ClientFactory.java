package main.dao.factory;

import java.sql.ResultSet;
import java.sql.SQLException;

import main.domain.Client;

public class ClientFactory {

    public static Client convert(ResultSet resultSet) throws SQLException {
        Client c = new Client();
		c.setId(resultSet.getLong("ID_CLIENTS"));
		c.setName(resultSet.getString(("NAME")));
		c.setCpf(resultSet.getLong(("CPF")));
		c.setFone(resultSet.getLong(("FONE")));
		c.setAddress(resultSet.getString(("ADDRESS")));
		c.setNumber(resultSet.getInt(("NUMBER")));
		c.setCity(resultSet.getString(("CITY")));
		c.setState(resultSet.getString(("STATE")));
        c.setCreationDate(resultSet.getTimestamp("CREATION_DATE"));
        return c;
    }
}

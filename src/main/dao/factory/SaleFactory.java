package main.dao.factory;

import java.sql.ResultSet;
import java.sql.SQLException;

import main.domain.Client;
import main.domain.Sale;
import main.domain.Sale.Status;

public class SaleFactory {
	public static Sale convert(ResultSet resultSet) throws SQLException {
		Client c = ClientFactory.convert(resultSet);
		Sale s = new Sale();
		s.setClient(c);
		s.setId(resultSet.getLong("ID_SALES"));
		s.setCode(resultSet.getString("CODE"));
		s.setTotalPrice(resultSet.getBigDecimal("TOTAL_PRICE"));
		s.setDate(resultSet.getTimestamp("DATE").toInstant());
		s.setStatus(Status.getByName(resultSet.getString("STATUS")));
		return s;
	}
}

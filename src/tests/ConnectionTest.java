package tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import main.dao.connection.ConnectionFactory;

public class ConnectionTest {
    @Test
    public void conexaoDeveSerValida() throws SQLException {
        Connection connection = ConnectionFactory.getConnection();
        assertNotNull(connection);
        assertFalse(connection.isClosed());
        connection.close();
        assertTrue(connection.isClosed());
    }
}

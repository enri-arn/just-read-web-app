package dao;

import com.mysql.jdbc.Connection;
import database.DatabaseConfig;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia
 */
public abstract class AbstractRepository<T, ID> implements Repository<T, ID> {

    protected Connection connection;
    protected PreparedStatement statement;
    protected ResultSet resultSet;

    /**
     * Connect to database.
     *
     * @param query : query for the prepared statement.
     * @throws SQLException
     */
    protected void connect(String query) throws SQLException {
        connection = (Connection) DriverManager.getConnection(DatabaseConfig.getUrl(), DatabaseConfig.getUser(), DatabaseConfig.getPassword());
        statement = connection.prepareStatement(query);
    }

    /**
     * Disconnect from database.
     *
     */
    protected void disconnect(){
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }
}

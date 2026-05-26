package dataaccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class MySQLAuthDAO implements AuthDAO {

    public MySQLAuthDAO() {
        try {
            DatabaseManager.configureDatabase();
        } catch (DataAccessException ex) {
            throw new RuntimeException("Unable to configure database");
        }
    }

    @Override
    public AuthData getAuth(String authToken) {
        return null;
    }

    @Override
    public AuthData addAuth(String username) throws DataAccessException {
        AuthData newData = new AuthData(UUID.randomUUID().toString(), username);
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO auth (username, authToken) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, newData.username());
                ps.setString(2, newData.authToken());
                ps.executeUpdate();
            }
        } catch (SQLException | DataAccessException ex) {
            throw new DataAccessException(ex.getMessage());
        }
        return newData;
    }

    @Override
    public void deleteAuth(String authToken) {

    }

    @Override
    public void clearAuth() {

    }
}

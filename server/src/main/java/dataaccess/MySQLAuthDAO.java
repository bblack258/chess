package dataaccess;

import model.AuthData;

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
    public AuthData addAuth(String username) {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) {

    }

    @Override
    public void clearAuth() {

    }
}

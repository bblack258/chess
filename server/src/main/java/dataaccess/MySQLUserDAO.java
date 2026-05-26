package dataaccess;

import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLUserDAO implements UserDAO {

    public MySQLUserDAO() {
        try {
            DatabaseManager.configureDatabase();
        } catch (DataAccessException ex) {
            throw new RuntimeException("Unable to configure database");
        }
    }

    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public void addUser(UserData u) throws AlreadyTakenException {

    }

    @Override
    public void clearUsers() {

    }


}

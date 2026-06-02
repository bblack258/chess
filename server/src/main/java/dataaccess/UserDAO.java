package dataaccess;

import model.UserData;
import dataaccesserrors.*;

public interface UserDAO {
    UserData getUser(String username) throws DataAccessException;
    void addUser(UserData u) throws DataAccessException;
    void clearUsers() throws DataAccessException;
}

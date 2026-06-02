package dataaccess;

import model.AuthData;
import dataaccesserrors.*;

public interface AuthDAO {
    AuthData getAuth(String authToken) throws DataAccessException;
    AuthData addAuth(String username) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    void clearAuth() throws DataAccessException;
}

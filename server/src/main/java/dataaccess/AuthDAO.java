package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData getAuth(String authToken);
    AuthData addAuth(String username) throws DataAccessException;
    void deleteAuth(String authToken);
    void clearAuth();
}

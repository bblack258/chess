package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData getAuth(String authToken);
    AuthData addAuth(String username);
    void deleteAuth(String authToken);
    void clearAuth();
}

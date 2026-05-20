package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {

    List<AuthData> authList = new ArrayList<>();

    @Override
    public AuthData getAuth(String authToken) {
        for (AuthData auth : authList) {
            if (authToken.equals(auth.authToken())) {
                return auth;
            }
        }
        return null;
    }

    @Override
    public AuthData addAuth(String username) {
        AuthData newData = new AuthData(UUID.randomUUID().toString(), username);
        authList.add(newData);
        return newData;
    }

    @Override
    public void deleteAuth(String authToken) {
        authList.removeIf(auth -> auth.authToken().equals(authToken));
    }

    @Override
    public void clearAuth() {
        authList = new ArrayList<>();
    }
}

package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class ClearService {

    private final UserDAO userMemory;
    private final AuthDAO authMemory;
    private final GameDAO gameMemory;

    public ClearService(UserDAO userMemory, AuthDAO authMemory, GameDAO gameMemory) {
        this.userMemory = userMemory;
        this.authMemory = authMemory;
        this.gameMemory = gameMemory;
    }

    public void clearAll() {
        userMemory.clearUsers();
        authMemory.clearAuth();
        gameMemory.clearGames();
    }
}

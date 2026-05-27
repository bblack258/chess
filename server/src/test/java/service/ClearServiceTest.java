package service;

import dataaccess.*;
import model.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClearServiceTest {

    @Test
    void clearAll() {
        List<GameData> expectedGame = new ArrayList<>();
        final UserDAO userMemory = new MySQLUserDAO();
        final AuthDAO authMemory = new MySQLAuthDAO();
        final GameDAO gameMemory = new MySQLGameDAO();

        final String fakeUser = "username";
        AuthData fakeAuth;

        try {
            userMemory.addUser(new UserData(fakeUser, null, null ));
            fakeAuth = authMemory.addAuth("username");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ClearService clear = new ClearService(userMemory, authMemory, gameMemory);
        clear.clearAll();

        List<GameData> actualGame;
        try {
            actualGame = gameMemory.getGames();
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        assertEquals(expectedGame, actualGame);

        try {
            assertNull(userMemory.getUser(fakeUser));
            assertNull(authMemory.getAuth(fakeAuth.authToken()));
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
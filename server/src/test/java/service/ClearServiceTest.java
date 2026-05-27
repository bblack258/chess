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

        final UserData fakeUser = new UserData("username", "password", "email" );
        AuthData fakeAuth;

        try {
            userMemory.addUser(fakeUser);
            fakeAuth = authMemory.addAuth("username");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ClearService clear = new ClearService(userMemory, authMemory, gameMemory);
        try {
            clear.clearAll();
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex.getMessage());
        }

        List<GameData> actualGame;
        try {
            actualGame = gameMemory.getGames();
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        assertEquals(expectedGame, actualGame);

        try {
            assertNull(userMemory.getUser(fakeUser.username()));
            assertNull(authMemory.getAuth(fakeAuth.authToken()));
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
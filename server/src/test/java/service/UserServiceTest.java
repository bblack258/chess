package service;

import dataaccess.*;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    UserDAO userMemory;
    AuthDAO authMemory;
    UserService service;
    UserData newUser;

    @BeforeEach
    void setup() {
        userMemory = new MySQLUserDAO();
        authMemory = new MySQLAuthDAO();
        service = new UserService(userMemory, authMemory);
        newUser = new UserData("username","password", "email");
    }

    @AfterEach
    void tearDown() {
        try {
            userMemory.clearUsers();
            authMemory.clearAuth();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void registerCorrect() {
        AuthData auth;
        try {
            auth = service.register(newUser);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        assertEquals("username", auth.username());
        assertNotNull(auth.authToken());
    }

    @Test
    void registerBad() {
        newUser = new UserData("username", null, "email");
        assertThrows(BadRequestException.class, () -> service.register(newUser));
    }

    @Test
    void registerTaken() {
        UserData secondUser = new UserData("username","otherPassword", "otherEmail");
        try {
            service.register(newUser);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        assertThrows(AlreadyTakenException.class, () -> service.register(secondUser));
    }

    @Test
    void loginCorrect() {
        AuthData auth;
        try {
            service.register(newUser);
            auth = service.login(newUser);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        assertEquals("username", auth.username());
        assertNotNull(auth.authToken());
    }

    @Test
    void loginUnauthorized() {
        try {
            service.register(newUser);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        UserData badUser = new UserData("username", "wrong", "email");
        assertThrows(UnauthorizedRequestException.class, () -> service.login(badUser));
    }

    @Test
    void logoutCorrect() {
        AuthData auth;
        try {
            auth = service.register(newUser);
            service.logout(auth.authToken());
            assertNull(authMemory.getAuth(auth.authToken()));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void logoutUnauthorized() {
        assertThrows(UnauthorizedRequestException.class, () -> service.logout(""));
    }
}
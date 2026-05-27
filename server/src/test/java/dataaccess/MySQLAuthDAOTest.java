package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class MySQLAuthDAOTest {

    static AuthDAO authMemory;
    static UserDAO userMemory;
    AuthData token;

    @BeforeAll
    static void prep() {
        authMemory = new MySQLAuthDAO();
        userMemory = new MySQLUserDAO();
    }

    @BeforeEach
    void setUp() {
        try {
            authMemory.clearAuth();
            token = authMemory.addAuth("username");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAuthCorrect() {
        try {
            AuthData myAuth = authMemory.getAuth(token.authToken());
            assertEquals(token, myAuth);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAuthIncorrect() {
        try {
            assertNull(authMemory.getAuth(null));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void addAuthCorrect() {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "Select authToken from auth where username = ?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, "username");
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        assertEquals(token.authToken(), rs.getString("authToken"));
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void addAuthIncorrect() {
        assertThrows(DataAccessException.class, () -> authMemory.addAuth(null));
    }

    @Test
    void deleteAuthCorrect() {
        try {
            authMemory.deleteAuth(token.authToken());
            assertNull(authMemory.getAuth(token.authToken()));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void deleteAuthIncorrect() {
        try {
            authMemory.deleteAuth("/clear");
            assertEquals(token, authMemory.getAuth(token.authToken()));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void clearAuth() {
        try {
            authMemory.clearAuth();
            assertNull(authMemory.getAuth(token.authToken()));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
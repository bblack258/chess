package dataaccess;

import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class MySQLUserDAOTest {

    static UserDAO userMemory;

    @BeforeAll
    static void prep() {
        userMemory = new MySQLUserDAO();
    }

    @BeforeEach
    void setUp() {
        try {
            userMemory.clearUsers();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getUserCorrect() {
        try {
            userMemory.addUser(new UserData("username", "password", "email"));
            UserData user = userMemory.getUser("username");
            assertEquals("password", user.password());
            assertEquals("email", user.email());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getUserIncorrect() {
        try {
            userMemory.addUser(new UserData("username", "password", "email"));
            UserData user = userMemory.getUser("notUsername");
            assertNull(user);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void addUserCorrect() {
        try {
            userMemory.addUser(new UserData("username", "password", "email"));
            try (Connection conn = DatabaseManager.getConnection()) {
                String statement = "Select password, email from user where username = ?";
                try (PreparedStatement ps = conn.prepareStatement(statement)) {
                    ps.setString(1, "username");
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            assertEquals("password", rs.getString("password"));
                            assertEquals("email", rs.getString("email"));
                        }
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void addUserIncorrect() {
            assertThrows(DataAccessException.class, () ->userMemory.addUser(new UserData("username",
                    null, "email")));
    }

    @Test
    void clearUsers() {
        try {
            assertNull(userMemory.getUser("username"));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
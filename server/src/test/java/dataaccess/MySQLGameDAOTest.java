package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MySQLGameDAOTest {

    static GameDAO gameMemory;
    GameData myGame = new GameData(1, null, null, "only", new ChessGame());

    @BeforeAll
    static void prep() {
        gameMemory = new MySQLGameDAO();
    }

    @BeforeEach
    void setUp() {
        try {
            gameMemory.clearGames();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getGamesCorrect() {
        try {
            gameMemory.addGame("only");
            assertEquals(List.of(myGame), gameMemory.getGames());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetGamesCorrect() {
        try {
            gameMemory.addGame("only");
            gameMemory.addGame("other");
            assertEquals(myGame, gameMemory.getGames(1));
            assertEquals(myGame, gameMemory.getGames("only"));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetGamesIncorrect() {
        try {
            assertNull(gameMemory.getGames(0));
            assertNull(gameMemory.getGames("none"));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void addGameCorrect() {
        try {
            gameMemory.addGame("only");
            try (Connection conn = DatabaseManager.getConnection()) {
                String statement = "SELECT gameID, whiteUsername, blackUsername, game FROM game WHERE gameName = ?";
                try (PreparedStatement ps = conn.prepareStatement(statement)) {
                    ps.setString(1, "only");
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            assertEquals(myGame.gameID(), rs.getInt("gameID"));
                            assertEquals(myGame.whiteUsername(), rs.getString("whiteUsername"));
                            assertEquals(myGame.blackUsername(), rs.getString("blackUsername"));
                            assertEquals(myGame.game(), new Gson().fromJson(rs.getString("game"), ChessGame.class));
                        }
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void addGameIncorrect() {
        assertThrows(DataAccessException.class, () -> gameMemory.addGame(null));
    }

    @Test
    void updateGameCorrect() {
        try {
            gameMemory.addGame("only");
            gameMemory.updateGame(1, ChessGame.TeamColor.WHITE, "bob");
            gameMemory.updateGame(1, ChessGame.TeamColor.BLACK, "bob bob");
            GameData updated = gameMemory.getGames(1);
            assertEquals("bob", updated.whiteUsername());
            assertEquals("bob bob", updated.blackUsername());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void updateGameIncorrect() {
        try {
            gameMemory.addGame("only");
            assertThrows(DataAccessException.class, () -> gameMemory.updateGame(1, null, "Jeff"));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void clearGames() {
        try {
            assertEquals(List.of(), gameMemory.getGames());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
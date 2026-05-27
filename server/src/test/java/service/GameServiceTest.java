package service;

import chess.ChessGame;
import dataaccess.*;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    AuthDAO authMemory;
    GameDAO gameMemory;
    GameService service;
    AuthData auth;

    @BeforeEach
    void setup() {
        authMemory = new MySQLAuthDAO();
        gameMemory = new MySQLGameDAO();
        try {
            auth = authMemory.addAuth("username");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        service = new GameService(authMemory, gameMemory);
    }

    @Test
    void addGameCorrect() {
        List<GameData> expected = new ArrayList<>();
        try {
            assertEquals(expected, gameMemory.getGames());
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        int gameID;
        try {
            gameID = service.addGame(auth.authToken(), "game1");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        expected.add(new GameData(gameID, null, null, "game1", new ChessGame()));
        try {
            assertEquals(expected, gameMemory.getGames());
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Test
    void addGameIncorrect() {
        assertThrows(UnauthorizedRequestException.class, () -> service.addGame("", "game1"));
        assertThrows(BadRequestException.class, () -> service.addGame(auth.authToken(), null));
        try {
            service.addGame(auth.authToken(), "game1");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        assertThrows(AlreadyExistsException.class, () -> service.addGame(auth.authToken(), "game1"));
    }


    @Test
    void joinGameCorrect() {
        int gameID;
        try {
            gameID = service.addGame(auth.authToken(), "game1");
            service.joinGame(auth.authToken(), "white", gameID);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try {
            assertEquals(auth.username(), gameMemory.getGames(gameID).whiteUsername());
            assertNull(gameMemory.getGames(gameID).blackUsername());
            assertEquals("game1", gameMemory.getGames(gameID).gameName());
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Test
    void joinGameIncorrect() {
        int gameID;
        try {
            gameID = service.addGame(auth.authToken(), "game1");
            service.joinGame(auth.authToken(), "white", gameID);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        assertThrows(UnauthorizedRequestException.class, () -> service.joinGame(null, "white", gameID));
        assertThrows(BadRequestException.class, () -> service.joinGame(auth.authToken(), null, gameID));
        assertThrows(AlreadyTakenException.class, () -> service.joinGame(auth.authToken(), "white", gameID));

    }

    @Test
    void listGamesCorrectEasy() {
        List<GameData> expected = new ArrayList<>();
        try {
            assertEquals(expected, service.listGames(auth.authToken()));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void listGamesCorrectHarder() {
        List<GameData> expected = new ArrayList<>();
        try {
            int gameID = service.addGame(auth.authToken(), "game1");
            ChessGame game = gameMemory.getGames(gameID).game();
            expected.add(new GameData(gameID, null, null, "game1", game));
            gameID = service.addGame(auth.authToken(), "game2");
            game = gameMemory.getGames(gameID).game();
            expected.add(new GameData(gameID, null, null, "game2", game));
            gameID = service.addGame(auth.authToken(), "game3");
            game = gameMemory.getGames(gameID).game();
            expected.add(new GameData(gameID, null, null, "game3", game));
            assertEquals(expected, service.listGames(auth.authToken()));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void listGamesIncorrect() {
        assertThrows(UnauthorizedRequestException.class, () -> service.listGames(null));
    }
}